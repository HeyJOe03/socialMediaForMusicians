
export const selectUserLogIn = (username: string, password: string) : string => {
    return /*sql*/`SELECT id FROM user WHERE username='${username}' AND hash_password='${password}'`
}

export const selectOneUsername = (username: string) : string => {
    return /*sql*/`SELECT 1 from user WHERE username='${username}'`
}

export const selectProfile = (id: number) : string => {
    return /*sql*/`SELECT username,name,email,description,instrument_interested_in,is_looking_someone_to_play_with FROM user WHERE id = ${id}`
}

export const selectProfilePicture = (id: number) : string => {
    return /*sql*/`SELECT profile_pic FROM user WHERE user.id = ${id}`
}

export const selectPostsInfo = (id: number) : string => {
    return /*sql*/`SELECT id,description,posted_by,created_at,last_update_at,author,title FROM posts WHERE posted_by = ${id}`
}

export const selectPostPicture = (id: number) : string => {
    return /*sql*/`SELECT content FROM posts WHERE posts.id = ${id}`
}