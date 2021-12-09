import User from "../../@types/user"

export const insertNewUser = (user: User) : string => {

    let keys = Object.keys(user).toString()
    let vlist = Object.values(user).toString()
    let av: string[] = vlist.split(',')
    for(let i = 0; i<av.length;i++) {
        av[i] = `'${av[i]}'`
        if(av[i] == '\'true\'') av[i] = 'true'
        else if(av[i] == '\'false\'') av[i] = 'false'
    }
    let values = av.join(',')
    let sql = /*sql*/`
        INSERT INTO user (${keys},created_at,last_profile_update,last_post,last_instrument_offer,last_music_sheet) 
        VALUES (${values},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)        
        `
    return sql
}