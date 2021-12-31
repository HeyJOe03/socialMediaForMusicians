export const deletePost = (id: Number): string => {
    return /*sql*/`DELETE FROM posts WHERE id = ${id}`
}