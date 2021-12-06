
const selectUserLogIn = (username: string, password: string) : string => {
    return /*sql*/`SELECT id FROM user WHERE username='${username}' AND hash_password='${password}'`
}

export = selectUserLogIn