import 'dotenv/config'
import mysql from 'mysql'

const options : mysql.ConnectionConfig = {
    user: process.env.DB_USER || 'root',
    host: process.env.DB_HOST || 'localhost',
    password: process.env.DB_PW || '',
    database:'socialmusician',
    port: Number(process.env.DB_PORT) || 3306
}

const DB : mysql.Connection = mysql.createConnection(process.env.DB_URI || options)

export default DB