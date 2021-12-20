
export const selectUserLogIn = (username: string, password: string) : string => {
    return /*sql*/`SELECT id FROM user WHERE username='${username}' AND hash_password='${password}'`
}

export const selectOneUsername = (username: string) : string => {
    return /*sql*/`SELECT 1 from user WHERE username='${username}'`
}

export const selectProfile = (id: number) : string => {
    return /*sql*/`SELECT user.id,username,profile_image,name,email,is_looking_someone_to_play_with,description,instrument_interested_in 
    FROM user LEFT JOIN aboutme ON user.id = aboutme.id WHERE user.id = ${id}`
}

export const selectProfilePicture = (id: number) : string => {
    return /*sql*/`SELECT profile_image FROM aboutme WHERE aboutme.id = ${id}`
}

export const selectPostsInfo = (id: number) : string => {
    return /*sql*/`SELECT id,description,posted_by,created_at,last_update_at,author,title FROM posts WHERE posted_by = ${id}`
}

export const selectPostPicture = (id: number) : string => {
    return /*sql*/`SELECT content FROM posts WHERE posts.id = ${id}`
}