import User from "../../@types/user"
import Profile from "../../@types/profile";

export const insertNewUser = (user: User) : string => {
    
    let sql = /*sql*/`
        INSERT INTO user (username,email,hash_password,name,is_looking_someone_to_play_with) 
        VALUES ('${user.username}','${user.email}','${user.hash_password}','${user.name}',${user.is_looking_someone_to_play_with})        
        `
    return sql
}

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

export const selectProfileFull = (id:number) : string => {
    return/*sql*/`SELECT id,username,email,hash_password,is_online,is_blocked,is_looking_someone_to_play_with,
    name,lat,lon,description,instrument_interested_in,created_at,last_post,last_instrument_offer,last_music_sheet
    FROM user
    WHERE id = ${id}`
}

export const updateProfile = (p: Profile) : string => {
    //console.log(p.is_looking_someone_to_play_with)
    return /*sql*/`UPDATE user SET 
        username = '${p.username}',
        email = '${p.email}',
        is_looking_someone_to_play_with = ${p.is_looking_someone_to_play_with},
        name='${p.name}',
        lat = ${p.lat || 'null'},
        lon = ${p.lon || 'null'},
        description = '${p.description}',
        instrument_interested_in = '${p.instrument_interested_in }'
        WHERE id = ${p.id}`
}