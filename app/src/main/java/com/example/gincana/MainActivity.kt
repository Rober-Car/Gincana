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




/**
 * MainActivity
 * ------------
 * QUÉ ES:
 * - Actividad principal de la aplicación.
 * - Pantalla que contiene el mapa de Google Maps y toda la lógica de interacción.
 *
 * QUÉ HACE:
 * - Inicializa el mapa.
 * - Carga iconos personalizados.
 * - Añade marcadores interactivos.
 * - Gestiona permisos de ubicación.
 * - Muestra diálogos de retos.
 * - Centra la cámara en puntos específicos.
 *
 * POR QUÉ EXISTE:
 * - Es el núcleo de la aplicación, donde se coordina toda la lógica visual y funcional.
 *
 * CONTEXTO TÉCNICO:
 * - Extiende AppCompatActivity → permite usar componentes modernos de Android.
 * - Implementa OnMapReadyCallback → obliga a implementar onMapReady(), donde se configura el mapa.
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    /**
     * binding
     * -------
     * QUÉ ES:
     * - Objeto generado automáticamente por ViewBinding a partir del layout activity_main.xml.
     * - Contiene referencias tipadas a todas las vistas del layout.
     *
     * QUÉ HACE:
     * - Permite acceder a las vistas sin usar findViewById().
     * - Evita errores de tipo y NullPointerException.
     *
     * POR QUÉ EXISTE:
     * - ViewBinding es el sistema moderno recomendado por Google para manejar vistas.
     */
    private lateinit var binding: ActivityMainBinding


    /**
     * map
     * ---
     * QUÉ ES:
     * - Objeto principal del SDK de Google Maps.
     * - Representa el mapa real mostrado en pantalla.
     *
     * QUÉ HACE:
     * - Añade marcadores.
     * - Mueve la cámara.
     * - Activa la ubicación del usuario.
     * - Aplica estilos visuales.
     * - Registra listeners de interacción.
     *
     * POR QUÉ EXISTE:
     * - Google Maps se carga de forma asíncrona y debe almacenarse aquí para usarlo después.
     *
     * RIESGOS:
     * - Si se usa antes de onMapReady(), la app se cierra.
     */
    private lateinit var map: GoogleMap


    /**
     * fusedLocationClient
     * -------------------
     * QUÉ ES:
     * - Proveedor de ubicación de Google Play Services.
     * - Sistema moderno recomendado para obtener la ubicación del dispositivo.
     *
     * QUÉ HACE:
     * - Obtiene la última ubicación conocida.
     * - Puede solicitar actualizaciones de ubicación.
     *
     * POR QUÉ EXISTE:
     * - Es más eficiente que usar directamente el GPS.
     * - Combina GPS, WiFi, Bluetooth y redes móviles para obtener la mejor ubicación posible.
     *
     * RIESGOS:
     * - Puede devolver null si no hay ubicación reciente.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    /**
     * onCreate()
     * ----------
     * QUÉ ES:
     * - Método del ciclo de vida de Android que se ejecuta al crear la actividad.
     *
     * QUÉ HACE:
     * - Infla el layout con ViewBinding.
     * - Obtiene el fragmento del mapa.
     * - Inicia la carga del mapa.
     * - Configura el Switch de ubicación.
     * - Inicializa el proveedor de ubicación.
     *
     * POR QUÉ EXISTE:
     * - Es el punto de entrada de toda la lógica de la actividad.
     * - Prepara todos los componentes antes de que el usuario interactúe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)


        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                activarUbicacion()
            } else {
                if (::map.isInitialized &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = false
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
     * - Carga iconos personalizados.
     * - Aplica un estilo visual.
     * - Añade marcadores.
     * - Centra la cámara.
     * - Configura listeners de interacción.
     *
     * POR QUÉ EXISTE:
     * - Es el único lugar donde el mapa está garantizado como listo para usarse.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        /**
         * Iconos personalizados
         * ---------------------
         * QUÉ ES:
         * - Conversión de imágenes PNG a Bitmaps escalados.
         *
         * QUÉ HACE:
         * - Crea iconos personalizados para los marcadores.
         *
         * POR QUÉ EXISTE:
         * - Mejora la estética del mapa.
         */
        val original = BitmapFactory.decodeResource(resources, R.drawable.astro_icon1)
        val icono = BitmapDescriptorFactory.fromBitmap(
            Bitmap.createScaledBitmap(original, 120, 120, false)
        )

        val original2 = BitmapFactory.decodeResource(resources, R.drawable.astro_icon2)
        val icono2 = BitmapDescriptorFactory.fromBitmap(
            Bitmap.createScaledBitmap(original2, 120, 120, false)
        )

        val original3 = BitmapFactory.decodeResource(resources, R.drawable.astro_icon3)
        val icono3 = BitmapDescriptorFactory.fromBitmap(
            Bitmap.createScaledBitmap(original3, 120, 120, false)
        )


        /**
         * Estilo del mapa
         * ---------------
         * QUÉ ES:
         * - Archivo JSON que define colores y apariencia del mapa.
         *
         * QUÉ HACE:
         * - Cambia colores de carreteras, agua, texto, terreno, etc.
         */
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success) println("Error aplicando estilo")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        /**
         * Coordenadas de ciudades
         * -----------------------
         * QUÉ ES:
         * - Conjunto de objetos LatLng que representan posiciones geográficas.
         *
         * QUÉ HACE:
         * - Define los puntos donde se colocarán los marcadores.
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
         * Marcadores de la gincana
         * ------------------------
         * QUÉ ES:
         * - Conjunto de puntos interactivos colocados en el mapa.
         *
         * QUÉ HACE:
         * - Muestran un título y una descripción.
         * - Permiten abrir un diálogo al pulsarlos.
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
                .icon(icono)
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
         * - Coordenada central aproximada de Andalucía.
         *
         * QUÉ HACE:
         * - Se usa como punto objetivo para centrar la cámara.
         */
        val andalucia = LatLng(37.5, -4.5)

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(andalucia, 7f))


        /**
         * Listener de clic en marcadores
         * ------------------------------
         * QUÉ ES:
         * - Callback que detecta cuando el usuario pulsa un marcador.
         *
         * QUÉ HACE:
         * - Abre un diálogo con el reto correspondiente.
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
     *
     * POR QUÉ EXISTE:
     * - Cada marcador representa un reto que debe validarse.
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
     * - Método encargado de gestionar permisos y activar la capa de ubicación del mapa.
     *
     * QUÉ HACE:
     * - Comprueba permisos.
     * - Activa la ubicación.
     * - Obtiene la última ubicación conocida.
     * - Mueve la cámara hacia la posición real del usuario.
     *
     * POR QUÉ EXISTE:
     * - Google Maps no puede mostrar la ubicación sin permisos explícitos.
     * - Centraliza toda la lógica de activación de ubicación.
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

            } else {
                Toast.makeText(this, "Buscando ubicación...", Toast.LENGTH_SHORT).show()

                val andalucia = LatLng(37.5, -4.5)
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(andalucia, 10f)
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
     * - Si fue denegado, desactiva el Switch y muestra un mensaje.
     *
     * POR QUÉ EXISTE:
     * - Android obliga a gestionar permisos en tiempo de ejecución.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            activarUbicacion()
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            binding.switchLocation.isChecked = false
        }
    }
}
