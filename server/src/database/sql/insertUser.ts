import User from "../../@types/user"
import Post from "../../@types/post"

export const insertNewUser = (user: User) : string => {
    
    let sql = /*sql*/`
        INSERT INTO user (username,email,hash_password,name,is_looking_someone_to_play_with) 
        VALUES ('${user.username}','${user.email}','${user.hash_password}','${user.name}',${user.is_looking_someone_to_play_with})        
        `
    return sql
}


export const insertNewPost = (post: Post) : string => {
    let sql = /*sql*/`
        INSERT INTO posts (author,title,description,posted_by,content) 
        VALUES ('${post.author}','${post.title}','${post.description}',${post.posted_by},?)`

    return sql
}