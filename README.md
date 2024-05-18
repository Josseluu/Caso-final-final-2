# Caso-final-final-2


https://github.com/Josseluu/Caso-final-final-2.git



**1. GestionCultivosDeBacterias (La Ventana Principal)**

Responsabilidad: Esta clase es la ventana principal de la aplicación donde puedes ver y manipular tus experimentos y poblaciones de bacterias.
Métodos Importantes:
configurarVentana():

Configura cómo se verá la ventana: tamaño, título, botones, menús, etc.
abrirExperimento():

Abre un archivo guardado que contiene un experimento anterior.
nuevoExperimento():

Crea un nuevo experimento desde cero.
guardarExperimento(boolean guardarComo):

Guarda el experimento actual en un archivo. Si guardarComo es verdadero, se pide al usuario que elija un nuevo archivo para guardar.
agregarPoblacion():

Añade una nueva población de bacterias al experimento actual.
borrarPoblacion():

Borra la población de bacterias seleccionada.
verInformacionPoblacion():

Muestra información detallada sobre la población de bacterias seleccionada.
realizarSimulacion():

Realiza una simulación de cómo crecerán las bacterias en la población seleccionada.


**2. Experimento (El Cuaderno del Experimento)**

Responsabilidad: Representa un experimento que contiene varias poblaciones de bacterias.
Atributos:
nombre: El nombre del experimento.
fechaInicio: La fecha en que se inició el experimento.
poblaciones: Una lista de todas las poblaciones de bacterias en este experimento.
archivo: El archivo donde se guarda el experimento.
Métodos Importantes:
agregarPoblacion(PoblacionDeBacterias poblacion):

Añade una población al experimento.
eliminarPoblacion(String nombre):

Elimina una población del experimento por su nombre.
obtenerPoblacion(String nombre):

Encuentra y devuelve una población específica por su nombre.


**3. PoblacionDeBacterias (Una Población de Bacterias)**

Responsabilidad: Representa una población de bacterias con su configuración y simulación.
Atributos:
nombre: El nombre de la población.
numeroInicialDeBacterias: El número inicial de bacterias en la población.
duracionDias: La duración del experimento en días.
patronComida: El patrón de alimentación de las bacterias.
fechaCreacion: La fecha en que se creó la población.
bacteriasPorDia: Una matriz que muestra cuántas bacterias hay en cada celda del plato cada día.
Métodos Importantes:
realizarSimulacion():

Simula cómo las bacterias crecerán y se moverán cada día.
getBacteriasPorDia():

Devuelve la matriz con el número de bacterias por día.
Ejemplo de Flujo de Uso

Abrir la Aplicación:

Verás la ventana principal con menús y botones.
Crear un Nuevo Experimento:

Presiona "Nuevo Experimento", ingresa un nombre, y empieza desde cero.
Añadir Poblaciones de Bacterias:

Presiona "Agregar Población", ingresa los detalles como nombre, número inicial de bacterias, etc.
Simular el Crecimiento de las Bacterias:

Selecciona una población de la lista y presiona "Simular". Verás cómo crecen y se mueven las bacterias día a día.
Guardar el Experimento:

Puedes guardar tu trabajo presionando "Guardar" o "Guardar como" si quieres especificar un nuevo archivo.


**CODIGO UML**

+-------------------------------+
| GestionCultivosDeBacterias    |
+-------------------------------+
| Ventana Principal              |
| -> Mostrar Experimentos        |
| -> Crear/Borrar Poblaciones    |
| -> Guardar/Abrir Experimentos  |
+-------------------------------+

+-------------------------------+
| Experimento                   |
+-------------------------------+
| Cuaderno de Experimentos      |
| -> Nombre                     |
| -> Fecha de Inicio            |
| -> Lista de Poblaciones       |
| -> Archivo Asociado           |
+-------------------------------+

+-------------------------------------+
| PoblacionDeBacterias                |
+-------------------------------------+
| Página del Cuaderno                 |
| -> Nombre                           |
| -> Número Inicial de Bacterias      |
| -> Duración del Experimento (días)  |
| -> Patrón de Comida                 |
| -> Fecha de Creación                |
| -> Matriz de Bacterias por Día      |
+-------------------------------------+
