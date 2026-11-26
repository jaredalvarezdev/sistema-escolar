
// Resolver registros DNS
const dns = require("node:dns/promises");
dns.setServers(["8.8.8.8", "8.8.4.4"]);

const { MongoClient, ObjectId, Double } = require("mongodb");
const bcrypt = require("bcryptjs");
const path = require("path");

require("dotenv").config({
    path: path.resolve(__dirname, ".env")
});

const uri = process.env.MONGO_URI || process.env.MONGODB_URI;
const password = process.env.SEED_PASSWORD;

const DB_NAME = "SistemaEscolarDB";

// Datos ficticios
const nombres = [
    "Juan", "Maria", "Carlos", "Ana", "Luis",
    "Sofia", "Diego", "Valeria", "Miguel", "Laura",
    "Pedro", "Daniela", "Jorge", "Fernanda", "David",
    "Andrea", "Ricardo", "Camila", "Eduardo", "Paola"
];

const apellidosPaternos = [
    "Garcia", "Martinez", "Hernandez",
    "Gonzalez", "Sanchez"
];

const apellidosMaternos = [
    "Lopez", "Rodriguez", "Perez",
    "Ramirez", "Torres"
];

const carreras = [
    "Tecnologias de la Informacion",
    "Mecatronica",
    "Administracion",
    "Logistica",
    "Desarrollo de Software"
];

const nombresMaterias = [
    "Programacion",
    "Matematicas",
    "Ingles",
    "Bases de Datos",
    "Redes",
    "Sistemas Operativos",
    "Desarrollo Web",
    "Estadistica",
    "Seguridad Informatica",
    "Inteligencia Artificial",
    "Programacion Orientada a Objetos",
    "Computacion en la Nube",
    "Ingenieria de Software",
    "Desarrollo Movil",
    "Algoritmos",
    "Estructuras de Datos",
    "Gestion de Proyectos",
    "Calidad de Software",
    "Administracion de Servidores",
    "Arquitectura de Software"
];

const dias = [
    "LUNES",
    "MARTES",
    "MIERCOLES",
    "JUEVES",
    "VIERNES",
    "SABADO"
];

// Colecciones
const usuarios = [];
const alumnos = [];
const materias = [];
const docentes = [];
const grupos = [];
const inscripciones = [];
const asistencias = [];
const calificaciones = [];

// Identificadores reproducibles
function generarId(coleccion, numero) {
    return new ObjectId(
        coleccion.toString(16).padStart(2, "0") +
        numero.toString(16).padStart(22, "0")
    );
}

// Convertir las referencias a String
function referencia(id) {
    return id.toHexString();
}

// Eliminar índices del modelo anterior
async function eliminarIndiceObsoleto(db, coleccion, nombre) {
    const existe = await db.listCollections({
        name: coleccion
    }).hasNext();

    if (!existe) return;

    const indices = await db.collection(coleccion).indexes();

    if (indices.some(indice => indice.name === nombre)) {

        await db.collection(coleccion).dropIndex(nombre);

        console.log(`Indice eliminado: ${coleccion}.${nombre}`);
    }
}

async function main() {

    if (!uri || !password) {
        throw new Error(
            "Configura MONGO_URI y SEED_PASSWORD"
        );
    }

    const client = new MongoClient(uri);
    const migrar = process.argv.includes("--migrate");

    try {

        await client.connect();

        const db = client.db(DB_NAME);

        console.log("Conexion exitosa a MongoDB Atlas");

        const hash = await bcrypt.hash(password, 10);

        // ==============================
        // 1. ALUMNOS
        // ==============================

        for (let i = 0; i < 20; i++) {

            alumnos.push({
                _id: generarId(2, i + 1),

                matricula: `A2026${String(i + 1).padStart(3, "0")}`,

                nombre: nombres[i],

                apellidoPaterno: apellidosPaternos[i % 5],

                apellidoMaterno: apellidosMaternos[i % 5],

                email: `alumno${i + 1}@example.com`,

                telefono: `477000${String(i + 1).padStart(4, "0")}`,

                fechaNacimiento: new Date(
                    Date.UTC(2004 + (i % 4), i % 12, i + 1)
                ),

                carrera: carreras[i % carreras.length],

                semestre: Math.floor(i / 4) + 1,

                activo: true
            });
        }

        // ==============================
        // 2. DOCENTES
        // ==============================

        for (let i = 0; i < 20; i++) {

            docentes.push({
                _id: generarId(4, i + 1),

                numeroEmpleado: `D${String(i + 1).padStart(4, "0")}`,

                nombre: nombres[i],

                apellidoPaterno: apellidosPaternos[i % 5],

                apellidoMaterno: apellidosMaternos[i % 5],

                email: `docente${i + 1}@example.com`,

                telefono: `477100${String(i + 1).padStart(4, "0")}`,

                especialidad: nombresMaterias[i],

                activo: true
            });
        }

        // ==============================
        // 3. MATERIAS
        // ==============================

        for (let i = 0; i < 20; i++) {

            materias.push({
                _id: generarId(3, i + 1),

                clave: `MAT${101 + i}`,

                nombre: nombresMaterias[i],

                descripcion: `Asignatura de ${nombresMaterias[i]}`,

                creditos: 6 + (i % 4),

                semestre: Math.floor(i / 4) + 1,

                horasSemana: 4,

                activo: true
            });
        }

        // ==============================
        // 4. USUARIOS
        // ==============================

        // Administrador
        usuarios.push({
            _id: generarId(1, 1),

            nombre: "Administrador del sistema",

            email: "admin@example.com",

            password: hash,

            rol: "ADMIN",

            alumnoId: null,

            docenteId: null,

            activo: true,

            fechaRegistro: new Date()
        });

        // Usuarios alumnos
        for (let i = 0; i < alumnos.length; i++) {

            const alumno = alumnos[i];

            usuarios.push({
                _id: generarId(1, i + 2),

                nombre: `${alumno.nombre} ${alumno.apellidoPaterno} ${alumno.apellidoMaterno}`,

                email: alumno.email,

                password: hash,

                rol: "ALUMNO",

                alumnoId: referencia(alumno._id),

                docenteId: null,

                activo: true,

                fechaRegistro: new Date()
            });
        }

        // Usuarios profesores
        for (let i = 0; i < docentes.length; i++) {

            const docente = docentes[i];

            usuarios.push({
                _id: generarId(1, i + 22),

                nombre: `${docente.nombre} ${docente.apellidoPaterno} ${docente.apellidoMaterno}`,

                email: docente.email,

                password: hash,

                rol: "PROFESOR",

                alumnoId: null,

                docenteId: referencia(docente._id),

                activo: true,

                fechaRegistro: new Date()
            });
        }

        // ==============================
        // 5. GRUPOS
        // ==============================

        for (let i = 0; i < 20; i++) {

            const horaInicio = i % 2 === 0
                ? "08:00"
                : "10:00";

            const horaFin = i % 2 === 0
                ? "10:00"
                : "12:00";

            grupos.push({
                _id: generarId(5, i + 1),

                clave: `${materias[i].clave}-A`,

                materiaId: referencia(materias[i]._id),

                docenteId: referencia(docentes[i]._id),

                periodo: "2026-3",

                cupoMaximo: 30,

                horarios: [
                    {
                        dia: dias[i % dias.length],
                        horaInicio,
                        horaFin,
                        aula: `B-${201 + i}`
                    },
                    {
                        dia: dias[(i + 2) % dias.length],
                        horaInicio,
                        horaFin,
                        aula: `B-${201 + i}`
                    }
                ],

                activo: true
            });
        }

        // ==============================
        // 6. INSCRIPCIONES
        // ==============================

        let numeroInscripcion = 1;

        for (let i = 0; i < alumnos.length; i++) {

            // Dos grupos del mismo semestre
            const indices = [i, i ^ 1];

            for (const indice of indices) {

                inscripciones.push({
                    _id: generarId(6, numeroInscripcion++),

                    alumnoId: referencia(alumnos[i]._id),

                    grupoId: referencia(grupos[indice]._id),

                    fechaInscripcion: new Date(
                        "2026-09-01T00:00:00Z"
                    ),

                    estado: "INSCRITO"
                });
            }
        }

        // ==============================
        // 7. ASISTENCIAS
        // ==============================

        const estadosAsistencia = [
            "PRESENTE",
            "PRESENTE",
            "PRESENTE",
            "FALTA",
            "RETARDO",
            "JUSTIFICADA"
        ];

        const observaciones = {
            PRESENTE: null,
            FALTA: "No asistio a clase",
            RETARDO: "Llegada tarde",
            JUSTIFICADA: "Inasistencia justificada"
        };

        let numeroAsistencia = 1;

        for (const inscripcion of inscripciones) {

            const grupo = grupos.find(
                g => referencia(g._id) === inscripcion.grupoId
            );

            const indiceDia = dias.indexOf(
                grupo.horarios[0].dia
            );

            for (let i = 0; i < 2; i++) {

                const fecha = new Date(
                    Date.UTC(2026, 8, 7 + indiceDia + i * 7)
                );

                const estado = estadosAsistencia[
                    numeroAsistencia % estadosAsistencia.length
                ];

                asistencias.push({
                    _id: generarId(7, numeroAsistencia++),

                    grupoId: inscripcion.grupoId,

                    alumnoId: inscripcion.alumnoId,

                    fecha,

                    estado,

                    observacion: observaciones[estado]
                });
            }
        }

        // ==============================
        // 8. CALIFICACIONES
        // ==============================

        let numeroCalificacion = 1;

        for (const inscripcion of inscripciones) {

            for (let parcial = 1; parcial <= 3; parcial++) {

                const nota = (
                    50 + (numeroCalificacion * 7) % 51
                ) / 10;

                calificaciones.push({
                    _id: generarId(8, numeroCalificacion++),

                    alumnoId: inscripcion.alumnoId,

                    grupoId: inscripcion.grupoId,

                    parcial,

                    calificacion: new Double(nota),

                    comentario: nota < 6
                        ? "Requiere mejorar su desempeno"
                        : "Buen desempeno academico",

                    fechaRegistro: new Date(
                        Date.UTC(2026, 8 + parcial - 1, 25, 12)
                    )
                });
            }
        }

        // ==============================
        // ORDEN DE INSERCION
        // ==============================

        const colecciones = {
            alumnos,
            docentes,
            materias,
            usuarios,
            grupos,
            inscripciones,
            asistencias,
            calificaciones
        };

        // Comprobar si existen registros anteriores
        for (const [nombre, documentos] of Object.entries(colecciones)) {

            const ids = documentos.map(doc => doc._id);

            const existentes = await db.collection(nombre)
                .countDocuments({
                    _id: { $in: ids }
                });

            if (existentes > 0 && !migrar) {

                throw new Error(
                    `Existen datos anteriores en ${nombre}. ` +
                    "Respalda los datos y utiliza --migrate."
                );
            }
        }

        // ==============================
        // ELIMINAR INDICES OBSOLETOS
        // ==============================

        if (migrar) {

            await eliminarIndiceObsoleto(
                db,
                "alumnos",
                "usuarioId_1"
            );

            await eliminarIndiceObsoleto(
                db,
                "docentes",
                "usuarioId_1"
            );

            await eliminarIndiceObsoleto(
                db,
                "asistencias",
                "alumnoId_1_grupoId_1_fecha_1"
            );
        }

        // ==============================
        // INSERTAR O ACTUALIZAR DOCUMENTOS
        // ==============================

        for (const [nombre, documentos] of Object.entries(colecciones)) {

            const coleccion = db.collection(nombre);

            await coleccion.bulkWrite(
                documentos.map(documento => ({
                    replaceOne: {
                        filter: {
                            _id: documento._id
                        },
                        replacement: documento,
                        upsert: true
                    }
                }))
            );

            console.log(`Coleccion procesada: ${nombre}`);
        }

        // ==============================
        // INDICES UNICOS
        // ==============================

        await db.collection("usuarios")
            .createIndex(
                { email: 1 },
                { unique: true }
            );

        await db.collection("alumnos")
            .createIndex(
                { matricula: 1 },
                { unique: true }
            );

        await db.collection("alumnos")
            .createIndex(
                { email: 1 },
                { unique: true }
            );

        await db.collection("docentes")
            .createIndex(
                { numeroEmpleado: 1 },
                { unique: true }
            );

        await db.collection("docentes")
            .createIndex(
                { email: 1 },
                { unique: true }
            );

        await db.collection("materias")
            .createIndex(
                { clave: 1 },
                { unique: true }
            );

        await db.collection("grupos")
            .createIndex(
                { clave: 1 },
                { unique: true }
            );

        await db.collection("inscripciones")
            .createIndex(
                { alumnoId: 1, grupoId: 1 },
                { unique: true }
            );

        await db.collection("asistencias")
            .createIndex(
                { grupoId: 1, alumnoId: 1, fecha: 1 },
                { unique: true }
            );

        await db.collection("calificaciones")
            .createIndex(
                { alumnoId: 1, grupoId: 1, parcial: 1 },
                { unique: true }
            );

        // ==============================
        // RESULTADOS
        // ==============================

        let total = 0;

        console.log("-----------------------------");

        for (const nombre of Object.keys(colecciones)) {

            const cantidad = await db.collection(nombre)
                .countDocuments();

            console.log(`${nombre}: ${cantidad} registros`);

            total += cantidad;
        }

        console.log("-----------------------------");
        console.log(`Total: ${total} registros`);
        console.log("Datos de prueba cargados correctamente");

    } finally {

        await client.close();
    }
}

main().catch(error => {

    console.error("Error:", error.message);

    process.exitCode = 1;
});
