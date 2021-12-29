import User from "../../@types/user"

export const insertNewUser = (user: User) : string => {
    
    let sql = /*sql*/`
        INSERT INTO user (username,email,hash_password,name,is_looking_someone_to_play_with) 
        VALUES ('${user.username}','${user.email}','${user.hash_password}','${user.name}',${user.is_looking_someone_to_play_with})        
        `
    return sql
}