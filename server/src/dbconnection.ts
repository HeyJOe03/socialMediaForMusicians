import 'dotenv/config'
import { ConnectionConfig } from 'mysql'

const options : ConnectionConfig = {
    user: process.env.DB_USER || 'root',
    host: process.env.DB_HOST || 'localhost',
    password: process.env.DB_PW || '',
    database:'socialmusician',
    port: Number(process.env.DB_PORT) || 3306
}
export default options