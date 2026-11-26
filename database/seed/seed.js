// Resolver registro de DNS
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

const nombres = [
    "Juan", "Maria", "Carlos", "Ana", "Luis",
    "Sofia", "Diego", "Valeria", "Miguel", "Laura",
    "Pedro", "Daniela", "Jorge", "Fernanda", "David",
    "Andrea", "Ricardo", "Camila", "Eduardo", "Paola"
];

const apellidos = [
    "Garcia Lopez",
    "Martinez Rodriguez",
    "Hernandez Perez",
    "Gonzalez Ramirez",
    "Sanchez Torres"
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
    "VIERNES"
];

const usuarios = [];
const alumnos = [];
const materias = [];
const docentes = [];
const grupos = [];
const inscripciones = [];
const asistencias = [];
const calificaciones = [];

// Identificadores reproducibles para evitar duplicados
function generarId(coleccion, numero) {
    return new ObjectId(
        coleccion.toString(16).padStart(2, "0") +
        numero.toString(16).padStart(22, "0")
    );
}

async function main() {

    if (!uri || !password) {
        throw new Error(
            "Configura MONGO_URI y SEED_PASSWORD"
        );
    }

    const client = new MongoClient(uri);

    try {

        await client.connect();

        const db = client.db(DB_NAME);

        console.log("Conexion exitosa a MongoDB Atlas");

        const hash = await bcrypt.hash(password, 10);

        // 1. USUARIO ADMINISTRADOR

        usuarios.push({
            _id: generarId(1, 1),
            email: "admin@example.com",
            password: hash,
            rol: "ADMINISTRADOR"
        });

        // 2. USUARIOS Y ALUMNOS

        for (let i = 0; i < 20; i++) {

            const usuarioId = generarId(1, i + 2);
            const email = `alumno${i + 1}@example.com`;

            usuarios.push({
                _id: usuarioId,
                email,
                password: hash,
                rol: "ALUMNO"
            });

            alumnos.push({
                _id: generarId(2, i + 1),
                usuarioId,
                matricula: `2026${String(i + 1).padStart(4, "0")}`,
                nombre: nombres[i],
                apellidos: apellidos[i % 5],
                email,
                semestre: Math.floor(i / 4) + 1
            });
        }

        // 3. MATERIAS

        for (let i = 0; i < 20; i++) {

            materias.push({
                _id: generarId(3, i + 1),
                clave: `MAT${String(i + 1).padStart(3, "0")}`,
                nombre: nombresMaterias[i],
                creditos: 6 + (i % 4),
                semestre: Math.floor(i / 4) + 1
            });
        }

        // 4. USUARIOS Y DOCENTES

        for (let i = 0; i < 20; i++) {

            const usuarioId = generarId(1, i + 22);
            const email = `docente${i + 1}@example.com`;

            usuarios.push({
                _id: usuarioId,
                email,
                password: hash,
                rol: "PROFESOR"
            });

            docentes.push({
                _id: generarId(4, i + 1),
                usuarioId,
                numeroEmpleado: `DOC${String(i + 1).padStart(3, "0")}`,
                nombre: `${nombres[i]} ${apellidos[i % 5]}`,
                email,
                especialidad: nombresMaterias[i]
            });
        }

        // 5. GRUPOS

        for (let i = 0; i < 20; i++) {

            grupos.push({
                _id: generarId(5, i + 1),
                materiaId: materias[i]._id,
                docenteId: docentes[i]._id,
                periodo: "2026-3",
                cupo: 30,
                horario: {
                    dia: dias[i % 5],
                    horaInicio: "08:00",
                    horaFin: "10:00",
                    aula: `A${101 + i}`
                }
            });
        }

        // 6. INSCRIPCIONES

        let numeroInscripcion = 1;

        for (let i = 0; i < alumnos.length; i++) {

            // Dos grupos del semestre del alumno
            const indices = [i, i ^ 1];

            for (const indice of indices) {

                inscripciones.push({
                    _id: generarId(6, numeroInscripcion++),
                    alumnoId: alumnos[i]._id,
                    grupoId: grupos[indice]._id,
                    fecha: new Date("2026-09-01T00:00:00Z"),
                    estado: "ACTIVA"
                });
            }
        }

        // 7. ASISTENCIAS

        let numeroAsistencia = 1;

        const estados = [
            "PRESENTE",
            "PRESENTE",
            "PRESENTE",
            "FALTA",
            "RETARDO"
        ];

        for (const inscripcion of inscripciones) {

            const grupo = grupos.find(g =>
                g._id.equals(inscripcion.grupoId)
            );

            const indiceDia = dias.indexOf(
                grupo.horario.dia
            );

            for (let i = 0; i < 2; i++) {

                const fecha = new Date(Date.UTC(
                    2026, 8, 7 + indiceDia + i * 7
                ));

                asistencias.push({
                    _id: generarId(7, numeroAsistencia),
                    alumnoId: inscripcion.alumnoId,
                    grupoId: inscripcion.grupoId,
                    fecha,
                    estado: estados[numeroAsistencia % 5]
                });

                numeroAsistencia++;
            }
        }

        // 8. CALIFICACIONES

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
                    calificacion: new Double(nota)
                });
            }
        }

        // INDICES UNICOS

        await db.collection("usuarios")
            .createIndex({ email: 1 }, { unique: true });

        await db.collection("alumnos")
            .createIndex({ matricula: 1 }, { unique: true });

        await db.collection("alumnos")
            .createIndex({ usuarioId: 1 }, { unique: true });

        await db.collection("materias")
            .createIndex({ clave: 1 }, { unique: true });

        await db.collection("docentes")
            .createIndex({ numeroEmpleado: 1 }, { unique: true });

        await db.collection("docentes")
            .createIndex({ usuarioId: 1 }, { unique: true });

        await db.collection("inscripciones")
            .createIndex(
                { alumnoId: 1, grupoId: 1 },
                { unique: true }
            );

        await db.collection("asistencias")
            .createIndex(
                { alumnoId: 1, grupoId: 1, fecha: 1 },
                { unique: true }
            );

        await db.collection("calificaciones")
            .createIndex(
                { alumnoId: 1, grupoId: 1, parcial: 1 },
                { unique: true }
            );

        // INSERTAR LOS DOCUMENTOS

        const colecciones = {
            usuarios,
            alumnos,
            materias,
            docentes,
            grupos,
            inscripciones,
            asistencias,
            calificaciones
        };

        let total = 0;

        for (const [nombre, documentos] of Object.entries(colecciones)) {

            const coleccion = db.collection(nombre);

            await coleccion.bulkWrite(
                documentos.map(documento => ({
                    updateOne: {
                        filter: { _id: documento._id },
                        update: { $setOnInsert: documento },
                        upsert: true
                    }
                }))
            );

            const cantidad = await coleccion.countDocuments();

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
