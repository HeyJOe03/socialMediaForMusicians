export const searchUserQuery = (s:string) : string => {
    return /*sql*/`SELECT id,username FROM user WHERE username LIKE '%${s}%'`
}