export const userLastTimeOnlineQuery = (id: Number) => {
    return /*sql*/ `SELECT last_time_online FROM user WHERE id = ${id}`
}

export const followedWithUpdateQuery = (id: Number, last_time_online: Number) => {
    return /*sql*/ `SELECT followed FROM follow WHERE follower = ${id}` // AND last_time_online < 
}

export const homeNewPostsQuery = (followId: Number, last_time_online: Number) => {
    return /*sql*/ `SELECT id FROM posts WHERE last_update_at < ${last_time_online} AND posted_by = ${followId}`
}