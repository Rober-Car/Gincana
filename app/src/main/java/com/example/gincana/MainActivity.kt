package com.example.gincana

import android.app.AlertDialog
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
     * Muestra un cuadro de diálogo cuando el usuario pulsa un marcador.
     *
     * Este método crea un AlertDialog con:
     * - Un título (nombre del marcador).
     * - Un mensaje (snippet del marcador).
     * - Un campo de texto para introducir una contraseña.
     * - Botón "Finalizar" que valida la contraseña.
     * - Botón "Cancelar" que cierra el diálogo.
     *
     * @param titulo Texto que aparecerá como título del diálogo.
     * @param descripcion Texto que aparecerá como mensaje del diálogo.
     */
    private fun mostrarDialogo(titulo: String?, descripcion: String?) {

        // AlertDialog.Builder permite construir un cuadro de diálogo paso a paso.
        val builder = AlertDialog.Builder(this)

        // Establece el título del diálogo.
        builder.setTitle(titulo)

        // EditText es un campo de texto donde el usuario puede escribir.
        val input = EditText(this)
        input.hint = "Introduce contraseña"

        // Establece el mensaje del diálogo.
        builder.setMessage(descripcion)

        // Inserta el campo de texto dentro del diálogo.
        builder.setView(input)

        // Botón positivo: se ejecuta cuando el usuario pulsa "Finalizar".
        builder.setPositiveButton("Finalizar") { dialog, which ->

            // Obtiene el texto introducido por el usuario.
            val pass = input.text.toString()

            // Comprueba si la contraseña es correcta.
            if (pass == "astro") {
                Toast.makeText(this, "Prueba completada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón negativo: cierra el diálogo sin hacer nada.
        builder.setNegativeButton("Cancelar", null)

        // Muestra el diálogo en pantalla.
        builder.show()
    }


}