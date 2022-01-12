import Post from "../../@types/post"

export const selectPostsID = (id: number) : string => {
    return /*sql*/`SELECT id FROM posts WHERE posted_by = ${id}`
}

export const selectPostInfo = (id: number) : string => {
    return /*sql*/`SELECT id,description,posted_by,created_at,last_update_at,author,title,likes FROM posts WHERE id = ${id}`
}

export const selectPostPicture = (id: number) : string => {
    return /*sql*/`SELECT content FROM posts WHERE posts.id = ${id}`
}

export const insertNewPost = (post: Post) : string => {
    let sql = /*sql*/`
        INSERT INTO posts (author,title,description,posted_by,content) 
        VALUES ('${post.author}','${post.title}','${post.description}',${post.posted_by},?)`

    return sql
}

export const deletePost = (id: Number): string => {
    return /*sql*/`DELETE FROM posts WHERE id = ${id}`
}