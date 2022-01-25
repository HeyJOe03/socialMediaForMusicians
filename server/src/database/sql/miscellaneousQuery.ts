export const updateLastTimeOnline = (id: Number) : string => {
    return /*sql*/`UPDATE user SET last_time_online = CURRENT_TIMESTAMP WHERE id = ${id}`
}