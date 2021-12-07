import User from "../../@types/user"

export const insertNewUser = (user: User) : string => {

    let keys = Object.keys(user).toString()
    let vlist = Object.values(user).toString()
    let av: string[] = vlist.split(',')
    for(let i = 0; i<av.length;i++) av[i] = `'${av[i]}'`
    let values = av.join(',')
    let sql = /*sql*/`
        INSERT INTO user (${keys}) VALUES (${values})        
        `
    return sql
}