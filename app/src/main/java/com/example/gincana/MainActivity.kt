package com.example.gincana

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gincana.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient




class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    /**
     * binding
     * -------
     * QUÉ ES:
     * - Instancia generada automáticamente por ViewBinding a partir del layout activity_main.xml.
     *
     * POR QUÉ EXISTE:
     * - Sustituye a findViewById(), evitando errores de tipo y referencias nulas.
     * - Garantiza acceso seguro y directo a todas las vistas del layout.
     *
     * QUÉ HACE:
     * - Permite acceder a elementos como switchLocation, el contenedor del mapa, etc.
     *
     * BENEFICIOS:
     * - Más rápido, más seguro y más limpio que usar IDs manuales.
     */
    private lateinit var binding: ActivityMainBinding


    /**
     * map
     * ---
     * QUÉ ES:
     * - Objeto principal del SDK de Google Maps que representa el mapa real mostrado en pantalla.
     *
     * POR QUÉ EXISTE:
     * - Google Maps no está disponible inmediatamente al iniciar la actividad.
     * - Solo se obtiene cuando el mapa termina de cargarse (onMapReady).
     *
     * QUÉ HACE:
     * - Permite añadir marcadores.
     * - Permite mover la cámara.
     * - Permite activar la ubicación del usuario.
     * - Permite registrar listeners de interacción (clicks, gestos, etc.).
     *
     * RIESGOS:
     * - Si se usa antes de onMapReady(), la app se cierra.
     */
    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    /**
     * onCreate()
     * ----------
     * QUÉ ES:
     * - Primer método que se ejecuta al crear la actividad.
     *
     * QUÉ HACE:
     * - Infla el layout con ViewBinding.
     * - Establece el contenido de la pantalla.
     * - Obtiene el fragmento del mapa.
     * - Inicia la carga asíncrona del mapa.
     * - Configura el Switch que activa/desactiva la ubicación del usuario.
     *
     * POR QUÉ ES IMPORTANTE:
     * - Es el punto de entrada de toda la lógica de la actividad.
     * - Prepara todo lo necesario antes de que el usuario interactúe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Infla el layout y crea el objeto binding. */
        binding = ActivityMainBinding.inflate(layoutInflater)

        /** Establece el layout como contenido visible de la actividad. */
        setContentView(binding.root)

        /**
         * mapFragment
         * -----------
         * QUÉ ES:
         * - Fragmento especial que contiene el mapa de Google.
         *
         * QUÉ HACE:
         * - Gestiona la creación del mapa.
         * - Permite solicitar el mapa mediante getMapAsync().
         *
         * POR QUÉ ES NECESARIO:
         * - Google Maps funciona dentro de un fragmento, no directamente en la actividad.
         */
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        /**
         * getMapAsync(this)
         * -----------------
         * QUÉ ES:
         * - Llamada que indica al fragmento que debe cargar el mapa.
         *
         * QUÉ HACE:
         * - Cuando el mapa está listo, invoca onMapReady(googleMap).
         *
         * POR QUÉ ES ASÍ:
         * - La carga del mapa es asíncrona y puede tardar varios segundos.
         */
        mapFragment.getMapAsync(this)


        /**
         * switchLocation.setOnCheckedChangeListener
         * -----------------------------------------
         * QUÉ ES:
         * - Listener que detecta cuando el usuario activa o desactiva el Switch.
         *
         * QUÉ HACE:
         * - Si se activa → intenta habilitar la ubicación del usuario.
         * - Si se desactiva → intenta deshabilitar la capa de ubicación.
         *
         * POR QUÉ ES NECESARIO:
         * - La ubicación requiere permisos y puede fallar.
         * - El usuario debe poder activar/desactivar la función manualmente.
         */
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                activarUbicacion()

            } else {
                @SuppressLint("MissingPermission")
                try {
                    /**
                     * map.isMyLocationEnabled = false
                     * -------------------------------
                     * QUÉ HACE:
                     * - Desactiva el punto azul de "mi ubicación".
                     *
                     * POR QUÉ ESTÁ EN TRY:
                     * - Si no hay permisos, podría lanzar SecurityException.
                     */
                    map.isMyLocationEnabled = false
                } catch (_: SecurityException) {
                    // Permiso no concedido, se ignora para evitar cierre de la app.
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    /**
     * onMapReady()
     * ------------
     * QUÉ ES:
     * - Método callback que se ejecuta cuando el mapa ha terminado de cargarse.
     *
     * QUÉ HACE:
     * - Guarda la referencia al mapa.
     * - Aplica un estilo visual personalizado.
     * - Define las coordenadas de las ciudades.
     * - Añade marcadores con título y descripción.
     * - Centra la cámara en Andalucía.
     * - Configura el listener de pulsación sobre marcadores.
     *
     * POR QUÉ ES IMPORTANTE:
     * - Es el único lugar donde el mapa está garantizado como listo para usarse.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        /** Guarda el mapa para poder usarlo en toda la actividad. */
        map = googleMap

        val original = BitmapFactory.decodeResource(resources, R.drawable.astro_icon1)
        val pequeño = Bitmap.createScaledBitmap(original, 120, 120, false)
        val icono = BitmapDescriptorFactory.fromBitmap(pequeño)

        val original2 = BitmapFactory.decodeResource(resources, R.drawable.astro_icon2)
        val pequeño2 = Bitmap.createScaledBitmap(original2, 120, 120, false)
        val icono2 = BitmapDescriptorFactory.fromBitmap(pequeño2)


        val original3 = BitmapFactory.decodeResource(resources, R.drawable.astro_icon3)
        val pequeño3 = Bitmap.createScaledBitmap(original3, 120, 120, false)
        val icono3 = BitmapDescriptorFactory.fromBitmap(pequeño3)


        /**
         * Estilo del mapa
         * ---------------
         * QUÉ ES:
         * - Un archivo JSON en /res/raw/map_style.json que define colores y apariencia del mapa.
         *
         * QUÉ HACE:
         * - Cambia colores de carreteras, agua, texto, terreno, etc.
         *
         * POR QUÉ ES ÚTIL:
         * - Permite personalizar el mapa para que combine con la estética de la app.
         */
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )

            if (!success) {
                println("Error aplicando estilo")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        /**
         * Coordenadas de todas las ciudades.
         * QUÉ ES:
         * - Cada LatLng representa una posición geográfica (latitud, longitud).
         *
         * POR QUÉ SE DEFINE AQUÍ:
         * - onMapReady es el momento en el que el mapa está listo para recibir marcadores.
         */
        val sevilla = LatLng(37.3891, -5.9845)
        val granada = LatLng(37.1773, -3.5986)
        val huelva = LatLng(37.2614, -6.9447)
        val cadiz = LatLng(36.5000, -6.2500)
        val malaga = LatLng(36.7202, -4.4203)
        val cordoba = LatLng(37.8882, -4.7794)
        val almeria = LatLng(36.8667, -2.3333)
        val jaen = LatLng(37.7796, -3.7849)
        val ronda = LatLng(36.7462, -5.1612)
        val bonares = LatLng(37.3236, -6.6789)


        /**
         * Marcadores
         * ----------
         * QUÉ SON:
         * - Iconos interactivos colocados en el mapa.
         *
         * QUÉ HACEN:
         * - Muestran un título y una descripción.
         * - Permiten abrir un diálogo al pulsarlos.
         *
         * POR QUÉ SE USAN:
         * - Representan puntos de misión de la gincana.
         */
        map.addMarker(
            MarkerOptions()
                .position(bonares)
                .title("Bonares – Inicio")
                .snippet("Empieza la misión. Busca el punto de energía escondido en el pueblo y actívalo.")
                 .icon(icono)
        )

        map.addMarker(
            MarkerOptions()
                .position(sevilla)
                .title("Sevilla – Giralda")
                .snippet("Sube hasta la torre y encuentra el robot perdido antes de que se apague.")
                .icon(icono2)
        )

        map.addMarker(
            MarkerOptions()
                .position(granada)
                .title("Granada – Alhambra")
                .snippet("Entra en la zona y recupera el chip que han escondido los enemigos.")
        )

        map.addMarker(
            MarkerOptions()
                .position(huelva)
                .title("Huelva – Puerto")
                .snippet("La nave está rota. Busca las piezas por el puerto y arréglala.")
                .icon(icono3)
        )

        map.addMarker(
            MarkerOptions()
                .position(cadiz)
                .title("Cádiz – Playa")
                .snippet("Ten cuidado con los robots del agua y elimina los que bloquean el camino.")
                .icon(icono)
        )

        map.addMarker(
            MarkerOptions()
                .position(malaga)
                .title("Málaga – Energía")
                .snippet("Recoge los cristales que hay por la zona para recargar a Astro.")
                .icon(icono2)
        )

        map.addMarker(
            MarkerOptions()
                .position(cordoba)
                .title("Córdoba – Laberinto")
                .snippet("Muévete por el laberinto sin caer en las trampas y encuentra la salida.")
                .icon(icono3)
        )

        map.addMarker(
            MarkerOptions()
                .position(almeria)
                .title("Almería – Desierto")
                .snippet("Aguanta la tormenta de arena y protege el sistema de Astro.")
                .icon(icono)
        )

        map.addMarker(
            MarkerOptions()
                .position(jaen)
                .title("Jaén – Olivos")
                .snippet("Busca la mina escondida entre los olivos y desactívala.")
                .icon(icono3)
        )

        map.addMarker(
            MarkerOptions()
                .position(ronda)
                .title("Ronda – Puente")
                .snippet("Cruza el puente sin que te detecten y activa el último punto.")
                .icon(icono2)
        )


        /**
         * andalucia
         * ---------
         * QUÉ ES:
         * - Coordenada aproximada del centro de Andalucía.
         *
         * QUÉ HACE:
         * - Se usa como punto objetivo para centrar la cámara.
         */
        val andalucia = LatLng(37.5, -4.5)

        /**
         * animateCamera()
         * ---------------
         * QUÉ HACE:
         * - Mueve la cámara con animación hacia la posición indicada.
         *
         * POR QUÉ SE USA:
         * - Da una sensación más suave y profesional al usuario.
         */
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(andalucia, 7f))


        /**
         * setOnMarkerClickListener
         * ------------------------
         * QUÉ ES:
         * - Listener que se ejecuta cuando el usuario pulsa un marcador.
         *
         * QUÉ HACE:
         * - Llama a mostrarDialogo() con la información del marcador.
         * - Devuelve true para indicar que gestionamos el evento manualmente.
         */
        map.setOnMarkerClickListener { marker ->

            mostrarDialogo(marker.title, marker.snippet)

            true
        }
    }


    /**
     * mostrarDialogo()
     * ----------------
     * QUÉ ES:
     * - Método que construye y muestra un AlertDialog personalizado.
     *
     * QUÉ HACE:
     * - Muestra título y descripción del reto.
     * - Añade un campo de texto para introducir una contraseña.
     * - Valida la contraseña "astro".
     * - Muestra mensajes de éxito o error.
     */
    private fun mostrarDialogo(titulo: String?, descripcion: String?) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(titulo)
        builder.setMessage(descripcion)

        val input = EditText(this)
        input.hint = "Introduce contraseña"

        builder.setView(input)

        builder.setPositiveButton("Finalizar", null)
        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()

        val boton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        boton.isEnabled = false

        input.addTextChangedListener {
            boton.isEnabled = it?.isNotEmpty() == true
        }

        boton.setOnClickListener {

            val pass = input.text.toString()

            if (pass == "astro") {
                Toast.makeText(this, "Prueba completada", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * activarUbicacion()
     * ------------------
     * QUÉ ES:
     * - Método que gestiona permisos y activa la ubicación del usuario.
     *
     * QUÉ HACE:
     * - Comprueba si el permiso está concedido.
     * - Si no lo está, lo solicita.
     * - Si lo está, activa la capa de ubicación del mapa.
     */
    @SuppressLint("MissingPermission")
    private fun activarUbicacion() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                val miUbicacion = LatLng(location.latitude, location.longitude)

                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(miUbicacion, 15f)
                )
            }
        }
    }


    /**
     * onRequestPermissionsResult()
     * ----------------------------
     * QUÉ ES:
     * - Método que recibe la respuesta del usuario al cuadro de permisos.
     *
     * QUÉ HACE:
     * - Si el permiso fue concedido, activa la ubicación del mapa.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            @SuppressLint("MissingPermission")
            try {
                map.isMyLocationEnabled = true

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {

                        val miUbicacion = LatLng(location.latitude, location.longitude)

                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(miUbicacion, 15f)
                        )
                    }
                }

            } catch (_: SecurityException) {}
        }
    }


}

