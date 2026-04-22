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
        // Bonares
        map.addMarker(MarkerOptions()
            .position(bonares)
            .title(getString(R.string.title_bonares))
            .snippet(getString(R.string.snippet_bonares))
            .icon(icono))

// Sevilla
        map.addMarker(MarkerOptions()
            .position(sevilla)
            .title(getString(R.string.title_sevilla))
            .snippet(getString(R.string.snippet_sevilla))
            .icon(icono2))

// Granada
        map.addMarker(MarkerOptions()
            .position(granada)
            .title(getString(R.string.title_granada))
            .snippet(getString(R.string.snippet_granada))
            .icon(icono))

// Huelva
        map.addMarker(MarkerOptions()
            .position(huelva)
            .title(getString(R.string.title_huelva))
            .snippet(getString(R.string.snippet_huelva))
            .icon(icono3))

// Cádiz
        map.addMarker(MarkerOptions()
            .position(cadiz)
            .title(getString(R.string.title_cadiz))
            .snippet(getString(R.string.snippet_cadiz))
            .icon(icono))

// Málaga
        map.addMarker(MarkerOptions()
            .position(malaga)
            .title(getString(R.string.title_malaga))
            .snippet(getString(R.string.snippet_malaga))
            .icon(icono2))

// Córdoba
        map.addMarker(MarkerOptions()
            .position(cordoba)
            .title(getString(R.string.title_cordoba))
            .snippet(getString(R.string.snippet_cordoba))
            .icon(icono3))

// Almería
        map.addMarker(MarkerOptions()
            .position(almeria)
            .title(getString(R.string.title_almeria))
            .snippet(getString(R.string.snippet_almeria))
            .icon(icono))

// Jaén
        map.addMarker(MarkerOptions()
            .position(jaen)
            .title(getString(R.string.title_jaen))
            .snippet(getString(R.string.snippet_jaen))
            .icon(icono3))

// Ronda
        map.addMarker(MarkerOptions()
            .position(ronda)
            .title(getString(R.string.title_ronda))
            .snippet(getString(R.string.snippet_ronda))
            .icon(icono2))

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



        map.setOnMarkerClickListener { marker ->
            mostrarDialogo(marker.title, marker.snippet)
            true
        }
    }
    /**
     * mostrarDialogo()
     * ----------------
     * QUÉ ES:
     * - Método responsable de construir, configurar y mostrar un AlertDialog personalizado.
     * - Es un componente de UI que aparece como una ventana emergente sobre la Activity.
     *
     * QUÉ HACE:
     * - Muestra un título y una descripción del reto.
     * - Inserta un campo de texto (EditText) para que el usuario introduzca una contraseña.
     * - Controla manualmente el botón "Finalizar" para que solo se active si hay texto.
     * - Valida la contraseña comparándola con un recurso de strings.
     * - Muestra mensajes de éxito o error mediante Toast.
     * - Cierra o mantiene abierto el diálogo según la validación.
     *
     * POR QUÉ EXISTE:
     * - Cada marcador del mapa representa una misión.
     * - Esta función permite que el usuario complete la misión introduciendo la contraseña correcta.
     *
     * DETALLES TÉCNICOS:
     * - Usa AlertDialog.Builder para construir el diálogo antes de crearlo.
     * - Usa setView() para incrustar un EditText dentro del diálogo.
     * - Usa listeners manuales para controlar cuándo se cierra el diálogo.
     * - Usa recursos de strings para permitir traducción y mantenimiento limpio.
     *
     * @param titulo Texto que aparecerá como cabecera del diálogo.
     * @param descripcion Texto que aparecerá como cuerpo del diálogo.
     */
    private fun mostrarDialogo(titulo: String?, descripcion: String?) {

        // 1. EL CONSTRUCTOR DEL DIÁLOGO (AlertDialog.Builder)
        // ---------------------------------------------------
        // QUÉ ES:
        // - Un objeto que permite configurar paso a paso un diálogo antes de crearlo.
        //
        // QUÉ HACE:
        // - Recibe un Context (this = la Activity actual).
        // - Permite definir título, mensaje, botones y vistas personalizadas.
        //
        // POR QUÉ EXISTE:
        // - Android separa la fase de "configuración" de la fase de "creación".
        val builder = AlertDialog.Builder(this)



        // 2. CONFIGURACIÓN DEL TEXTO DEL DIÁLOGO
        // --------------------------------------
        // QUÉ ES:
        // - Asignación del título y mensaje que se mostrarán en la ventana.
        //
        // QUÉ HACE:
        // - Usa los parámetros recibidos para personalizar el diálogo según el marcador pulsado.
        builder.setTitle(titulo)
        builder.setMessage(descripcion)



        // 3. CREACIÓN DEL CAMPO DE TEXTO (EditText)
        // -----------------------------------------
        // QUÉ ES:
        // - Un componente de entrada donde el usuario puede escribir texto.
        //
        // QUÉ HACE:
        // - Permite introducir la contraseña necesaria para completar la misión.
        //
        // POR QUÉ EXISTE:
        // - Los diálogos por defecto NO incluyen campos de texto; hay que crearlos manualmente.
        val input = EditText(this)



        // 4. ASIGNACIÓN DEL HINT (texto gris dentro del EditText)
        // --------------------------------------------------------
        // QUÉ ES:
        // - Un texto guía que aparece dentro del EditText cuando está vacío.
        //
        // QUÉ HACE:
        // - Indica al usuario qué debe escribir.
        //
        // POR QUÉ EXISTE:
        // - Mejora la usabilidad y claridad del diálogo.
        input.hint = getString(R.string.dialog_hint_password)



        // 5. INSERTAR EL EDITTEXT EN EL DIÁLOGO
        // -------------------------------------
        // QUÉ ES:
        // - setView() permite incrustar una vista personalizada dentro del diálogo.
        //
        // QUÉ HACE:
        // - Coloca el EditText dentro del cuerpo del diálogo.
        //
        // POR QUÉ EXISTE:
        // - Los diálogos estándar solo muestran texto; esta función permite personalizarlos.
        builder.setView(input)



        // 6. CONFIGURACIÓN DE BOTONES
        // ---------------------------
        // QUÉ ES:
        // - Definición de los botones "Finalizar" y "Cancelar".
        //
        // QUÉ HACE:
        // - Asigna el texto de los botones usando recursos de strings.
        // - El listener se deja en null porque queremos controlarlo manualmente después.
        //
        // POR QUÉ EXISTE:
        // - Si pusiéramos un listener aquí, el diálogo se cerraría automáticamente.
        builder.setPositiveButton(getString(R.string.dialog_button_finish), null)
        builder.setNegativeButton(getString(R.string.dialog_button_cancel), null)



        // 7. CREAR Y MOSTRAR EL DIÁLOGO
        // ------------------------------
        // QUÉ ES:
        // - create() construye el diálogo con toda la configuración previa.
        // - show() lo dibuja en pantalla.
        //
        // POR QUÉ EXISTE:
        // - Hasta que no se llama a show(), el diálogo no existe visualmente.
        val dialog = builder.create()
        dialog.show()



        // 8. OBTENER REFERENCIA AL BOTÓN POSITIVO
        // ---------------------------------------
        // QUÉ ES:
        // - getButton() permite acceder al botón físico del diálogo.
        //
        // QUÉ HACE:
        // - Permite modificar su estado (habilitado/deshabilitado).
        //
        // POR QUÉ EXISTE:
        // - Necesitamos controlar cuándo el usuario puede pulsarlo.
        val boton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)



        // 9. DESACTIVAR EL BOTÓN AL INICIO
        // --------------------------------
        // QUÉ ES:
        // - isEnabled controla si el botón está activo.
        //
        // QUÉ HACE:
        // - Evita que el usuario pulse "Finalizar" sin escribir nada.
        boton.isEnabled = false



        // 10. ESCUCHADOR DEL TEXTO DEL EDITTEXT
        // -------------------------------------
        // QUÉ ES:
        // - addTextChangedListener escucha cada cambio en el texto del EditText.
        //
        // QUÉ HACE:
        // - Activa el botón solo si hay texto.
        //
        // POR QUÉ EXISTE:
        // - Evita validaciones vacías.
        input.addTextChangedListener {
            boton.isEnabled = it?.isNotEmpty() == true
        }



        // 11. ESCUCHADOR DEL BOTÓN "FINALIZAR"
        // ------------------------------------
        // QUÉ ES:
        // - Listener que se ejecuta cuando el usuario toca el botón.
        //
        // QUÉ HACE:
        // - Obtiene el texto del EditText.
        // - Lo compara con la contraseña correcta.
        // - Muestra un Toast de éxito o error.
        // - Cierra el diálogo solo si la contraseña es correcta.
        boton.setOnClickListener {

            val pass = input.text.toString()

            // 12. VALIDACIÓN DE CONTRASEÑA
            // ----------------------------
            // QUÉ ES:
            // - Comparación entre el texto introducido y el valor almacenado en strings.xml.
            //
            // QUÉ HACE:
            // - Si coincide → misión completada.
            // - Si no coincide → error.
            if (pass == getString(R.string.password_correct)) {

                // 13. TOAST DE ÉXITO
                // ------------------
                Toast.makeText(this, getString(R.string.toast_mission_completed), Toast.LENGTH_SHORT).show()

                // 14. CIERRE DEL DIÁLOGO
                // ----------------------
                dialog.dismiss()

            } else {

                // TOAST DE ERROR (el diálogo permanece abierto)
                Toast.makeText(this, getString(R.string.toast_wrong_password), Toast.LENGTH_SHORT).show()
            }
        }
    }



    /**
     * activarUbicacion()
     * ------------------
     * QUÉ ES:
     * - Método encargado de gestionar todo el proceso necesario para activar la capa de ubicación
     *   del mapa y centrar la cámara en la posición real del usuario.
     *
     * QUÉ HACE:
     * - Comprueba si el permiso ACCESS_FINE_LOCATION está concedido.
     * - Si no lo está, solicita el permiso al usuario y detiene la ejecución.
     * - Si el permiso está concedido:
     *      1. Activa la capa de ubicación del mapa (punto azul).
     *      2. Solicita la última ubicación conocida mediante FusedLocationProviderClient.
     *      3. Si existe ubicación → mueve la cámara a esa posición con zoom 15.
     *      4. Si NO existe ubicación → aplica un fallback centrando la cámara en Andalucía.
     *
     * POR QUÉ EXISTE:
     * - Google Maps no puede mostrar la ubicación del usuario sin permisos explícitos.
     * - La ubicación puede ser null si el dispositivo no tiene datos recientes (modo avión, GPS apagado, reinicio, etc.).
     * - Este método centraliza toda la lógica necesaria para activar la ubicación de forma segura.
     *
     * CÓMO FUNCIONA INTERNAMENTE:
     * - FusedLocationProviderClient consulta varias fuentes (GPS, WiFi, redes móviles, sensores)
     *   y devuelve la última ubicación conocida sin necesidad de activar el GPS continuamente.
     * - map.isMyLocationEnabled activa la capa visual del punto azul.
     * - CameraUpdateFactory.newLatLngZoom crea una animación de cámara hacia la coordenada indicada.
     *
     * RIESGOS:
     * - Si se llama sin permisos → SecurityException.
     * - Si location es null → no se puede centrar la cámara en el usuario.
     * - Si el GPS está desactivado, la ubicación puede tardar en aparecer.
     *
     * NOTA:
     * - El fallback evita que la cámara se quede quieta cuando no hay ubicación disponible.
     */
    @SuppressLint("MissingPermission")
    private fun activarUbicacion() {

        // 1. COMPROBACIÓN DE PERMISOS
        // ---------------------------
        // QUÉ ES:
        // - checkSelfPermission revisa si el usuario ha concedido el permiso de ubicación fina.
        //
        // QUÉ HACE:
        // - Si el permiso NO está concedido → se solicita al usuario.
        //
        // POR QUÉ EXISTE:
        // - Android obliga a pedir permisos en tiempo de ejecución para proteger la privacidad.
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Solicita el permiso al usuario.
            // requestPermissions lanza un cuadro de diálogo del sistema.
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }


        // 2. ACTIVAR LA CAPA DE UBICACIÓN DEL MAPA
        // -----------------------------------------
        // QUÉ ES:
        // - isMyLocationEnabled activa el punto azul y el botón de centrar ubicación.
        //
        // QUÉ HACE:
        // - Muestra la ubicación del usuario en el mapa.
        //
        // RIESGO:
        // - Si no hay permisos, lanza SecurityException (por eso se comprueba antes).
        map.isMyLocationEnabled = true


        // 3. OBTENER LA ÚLTIMA UBICACIÓN CONOCIDA
        // ----------------------------------------
        // QUÉ ES:
        // - lastLocation es una llamada asíncrona que devuelve la última ubicación registrada.
        //
        // QUÉ HACE:
        // - Si existe → devuelve un objeto Location.
        // - Si no existe → devuelve null.
        //
        // POR QUÉ EXISTE:
        // - Es más rápido que pedir una ubicación nueva.
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            // 4. SI EXISTE UBICACIÓN REAL
            // ---------------------------
            if (location != null) {

                // Convertimos la ubicación a LatLng (formato de Google Maps).
                val miUbicacion = LatLng(location.latitude, location.longitude)

                // Movemos la cámara hacia la ubicación real del usuario.
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(miUbicacion, 15f)
                )

            } else {

                // 5. FALLBACK CUANDO LA UBICACIÓN ES NULL
                // ----------------------------------------
                // QUÉ ES:
                // - Un mensaje temporal que informa al usuario.
                //
                // POR QUÉ EXISTE:
                // - La ubicación puede ser null si:
                //   * El GPS está apagado
                //   * El móvil acaba de encenderse
                //   * No hay datos recientes
                //   * Está en interiores sin señal
                Toast.makeText(this, "Buscando ubicación...", Toast.LENGTH_SHORT).show()

                // Coordenada alternativa para no dejar la cámara quieta.
                val andalucia = LatLng(37.5, -4.5)

                // Movemos la cámara a un punto general.
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
