import Post from "../../@types/post"

export const selectPostsIDQuery = (id: number) : string => {
    return /*sql*/`SELECT id FROM posts WHERE posted_by = ${id}`
}

export const selectPostInfoQuery = (id: number) : string => {
    return /*sql*/`SELECT id,description,posted_by,created_at,last_update_at,author,title,likes FROM posts WHERE id = ${id}`
}

export const selectPostPictureQuery = (id: number) : string => {
    return /*sql*/`SELECT content FROM posts WHERE posts.id = ${id}`
}

export const insertNewPostQuery = (post: Post) : string => {
    let sql = /*sql*/`
        INSERT INTO posts (author,title,description,posted_by,content) 
        VALUES ('${post.author}','${post.title}','${post.description}',${post.posted_by},?)`

    return sql
}

export const deletePostQuery = (id: Number): string => {
    return /*sql*/`DELETE FROM posts WHERE id = ${id}`
}

export const updatePostQuery = (id: Number,author:string,title:string,description:string): string => {
    return /*sql*/ `UPDATE posts SET author = '${author}',title = '${title}', description = '${description}',last_update_at = CURRENT_TIMESTAMP WHERE id = ${id};`
}