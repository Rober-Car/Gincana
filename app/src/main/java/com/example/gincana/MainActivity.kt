package com.example.gincana

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
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


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding: ActivityMainBinding

    /**
     * Referencia al mapa de Google una vez que ha sido inicializado.
     * No debe usarse antes de que onMapReady sea ejecutado.
     */
    private lateinit var map: GoogleMap

    /**
     * Método del ciclo de vida de la actividad que se ejecuta al crearla.
     *
     * Acciones realizadas:
     * - Se infla el layout mediante ViewBinding.
     * - Se establece el layout como vista principal.
     * - Se obtiene el fragmento del mapa desde el XML.
     * - Se solicita la inicialización asíncrona del mapa.
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Obtiene el fragmento del mapa definido en el archivo XML.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        // Solicita que el mapa se cargue de forma asíncrona.
        // Cuando esté listo, se llamará automáticamente a onMapReady().
        mapFragment.getMapAsync(this)

        /**
         * Listener que detecta los cambios en el interruptor (Switch) encargado de activar o desactivar
         * la visualización de la ubicación del usuario en el mapa.
         *
         *setOnCheckedChangeListener:
         *     Listener que se ejecuta cada vez que el usuario cambia el estado del Switch.
         *
         * Comportamiento:
         *     - Si el Switch está activado, se llama al método activarUbicacion(), que se encarga
         *       de solicitar permisos y activar la capa de ubicación del mapa.
         *
         *     - Si el Switch está desactivado, se deshabilita la capa de ubicación del mapa
         *       mediante map.isMyLocationEnabled = false.
         */
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->

            // Si el usuario activa el Switch, se intenta activar la ubicación.
            if (isChecked) {
                activarUbicacion()

                // Si el usuario lo desactiva, se oculta la ubicación en el mapa.
            } else {
                map.isMyLocationEnabled = false
            }
        }

    }

    /**
     * Método callback que se ejecuta automáticamente cuando el mapa ha sido inicializado.
     *
     * Este método recibe un objeto GoogleMap ya listo para ser utilizado.
     * Aquí se configuran:
     * - Las referencias a las ciudades (LatLng).
     * - Los marcadores que aparecerán en el mapa.
     * - El movimiento inicial de la cámara.
     * - El listener que detecta pulsaciones sobre los marcadores.
     *
     *  googleMap Objeto GoogleMap completamente inicializado.
     *                  Representa el mapa sobre el que se dibujan marcadores,
     *                  se mueve la cámara y se gestionan interacciones.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        // Guarda la referencia al mapa para poder usarlo en otros métodos.
        // "map" es una variable de clase que almacenará el mapa ya listo.
        map = googleMap

        // LatLng representa una coordenada geográfica (latitud, longitud).
        // Cada variable almacena la posición de una ciudad o pueblo.
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

        // MarkerOptions define cómo será un marcador:
        // - posición
        // - título
        // - snippet (texto adicional)
        // map.addMarker() añade el marcador al mapa.
        map.addMarker(
            MarkerOptions()
                .position(bonares)
                .title("Bonares - Prueba")
                .snippet("Reto en el pueblo")
        )

        map.addMarker(
            MarkerOptions()
                .position(sevilla)
                .title("Sevilla - Prueba 1")
                .snippet("Encuentra el robot")
        )

        map.addMarker(
            MarkerOptions()
                .position(granada)
                .title("Granada - Prueba 2")
                .snippet("Sube la Alhambra")
        )

        map.addMarker(
            MarkerOptions()
                .position(huelva)
                .title("Huelva - Prueba 3")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(cadiz)
                .title("Cádiz - Prueba 4")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(malaga)
                .title("Málaga - Prueba 5")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(cordoba)
                .title("Córdoba - Prueba 6")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(almeria)
                .title("Almería - Prueba 7")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(jaen)
                .title("Jaén - Prueba 8")
                .snippet("Pendiente")
        )

        map.addMarker(
            MarkerOptions()
                .position(ronda)
                .title("Ronda - Prueba")
                .snippet("El puente secreto")
        )

        // Coordenada central aproximada de Andalucía.
        val andalucia = LatLng(37.5, -4.5)

        // Mueve la cámara con animación hacia la posición indicada.
        // newLatLngZoom define la posición y el nivel de zoom.
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(andalucia, 7f))

        // Listener que detecta cuando el usuario pulsa un marcador.
        // marker -> referencia al marcador pulsado.
        // Devolvemos true para indicar que gestionamos el evento manualmente.
        map.setOnMarkerClickListener { marker ->

            // Llama al método que muestra un diálogo con título y descripción del marcador.
            mostrarDialogo(marker.title, marker.snippet)

            true
        }
    }

    /**
     * Muestra un cuadro de diálogo personalizado cuando el usuario pulsa un marcador.
     *
     * Este diálogo incluye:
     * - Un título (nombre del marcador).
     * - Un mensaje descriptivo (snippet del marcador).
     * - Un campo de texto para introducir una contraseña.
     * - Un botón "Finalizar" que solo se activa cuando el usuario escribe algo.
     * - Un botón "Cancelar" que cierra el diálogo.
     *
     * @param titulo Texto que aparecerá como título del diálogo.
     * @param descripcion Texto que aparecerá como mensaje del diálogo.
     */
    private fun mostrarDialogo(titulo: String?, descripcion: String?) {

        // AlertDialog.Builder:
        // Clase que permite construir un cuadro de diálogo paso a paso.
        val builder = AlertDialog.Builder(this)

        // Establece el título del diálogo usando el texto recibido.
        builder.setTitle(titulo)

        // Establece el mensaje del diálogo (descripción del marcador).
        builder.setMessage(descripcion)

        // EditText:
        // Campo de texto donde el usuario puede escribir la contraseña.
        val input = EditText(this)

        // hint:
        // Texto gris que aparece dentro del EditText para indicar qué debe escribir el usuario.
        input.hint = "Introduce contraseña"

        // Inserta el EditText dentro del diálogo.
        builder.setView(input)

        // Botón positivo ("Finalizar"):
        // Se pasa null porque luego lo sobreescribimos manualmente para controlar su comportamiento.
        builder.setPositiveButton("Finalizar", null)

        // Botón negativo ("Cancelar"):
        // null indica que simplemente cerrará el diálogo sin hacer nada.
        builder.setNegativeButton("Cancelar", null)

        // Crea el diálogo real a partir del builder.
        val dialog = builder.create()

        // Muestra el diálogo en pantalla.
        dialog.show()

        // Obtiene la referencia al botón "Finalizar" ya creado dentro del diálogo.
        val boton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        // Desactiva el botón inicialmente.
        // Esto evita que el usuario pulse "Finalizar" sin escribir nada.
        boton.isEnabled = false

        // addTextChangedListener:
        // Listener que detecta cambios en el texto del EditText.
        // "it" representa el texto actual.
        input.addTextChangedListener {
            // Activa el botón solo si el texto NO está vacío.
            boton.isEnabled = it?.isNotEmpty() == true
        }

        // Configura manualmente lo que ocurre al pulsar el botón "Finalizar".
        boton.setOnClickListener {

            // Obtiene el texto escrito por el usuario.
            val pass = input.text.toString()

            // Comprueba si la contraseña es correcta.
            if (pass == "astro") {

                // Muestra un mensaje indicando que la prueba se completó.
                Toast.makeText(this, "Prueba completada", Toast.LENGTH_SHORT).show()

                // Cierra el diálogo.
                dialog.dismiss()

            } else {

                // Mensaje de error si la contraseña no coincide.
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Activa la capa de ubicación del mapa si el usuario ha concedido el permiso.
     *
     * Este método realiza dos tareas principales:
     *
     * 1. Comprobar si el permiso ACCESS_FINE_LOCATION está concedido.
     * 2. Si no lo está, solicitarlo al usuario.
     * 3. Si ya está concedido, activar la visualización de la ubicación en el mapa.
     *
     */
    private fun activarUbicacion() {

        // checkSelfPermission:
        // Método que comprueba si un permiso específico está concedido.
        // Devuelve PackageManager.PERMISSION_GRANTED si el permiso está aprobado.
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // requestPermissions:
            // Método que muestra al usuario la ventana del sistema para aceptar o denegar permisos.
            // El primer parámetro es un array con los permisos solicitados.
            // El segundo parámetro es un código de solicitud (requestCode) para identificar la respuesta.
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )

            // return:
            // Sale del método para esperar la respuesta del usuario.
            return
        }

        // map.isMyLocationEnabled:
        // Propiedad del objeto GoogleMap que activa la capa de ubicación.
        // Cuando está en true, el mapa muestra un punto azul indicando la posición del usuario.
        map.isMyLocationEnabled = true
    }

    /**
     * Método callback que se ejecuta automáticamente cuando el usuario responde
     * a una solicitud de permisos mostrada previamente con requestPermissions().
     *
     *
     * Funcionamiento:
     * 1. Comprueba que el requestCode coincide con el permiso de ubicación.
     * 2. Comprueba que el usuario ha respondido y que el permiso ha sido concedido.
     * 3. Si el permiso está concedido, activa la capa de ubicación del mapa.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Llama al comportamiento por defecto del método en la clase padre.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Comprueba:
        // - Que el código de solicitud es el esperado (1).
        // - Que el array de resultados no está vacío.
        // - Que el primer resultado indica que el permiso fue concedido.
        if (requestCode == 1 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // Activa la capa de ubicación del mapa.
            // Esto muestra el punto azul con la posición del usuario.
            map.isMyLocationEnabled = true
        }
    }



}