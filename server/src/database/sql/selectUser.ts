
export const selectUserLogIn = (username: string, password: string) : string => {
    return /*sql*/`SELECT id FROM user WHERE username='${username}' AND hash_password='${password}'`
}

export const selectOneUsername = (username: string) : string => {
    return /*sql*/`SELECT 1 from user WHERE username='${username}'`
}