interface User{
    id? : number
    username:string
    name:string
    email:string
    hash_password:string
    lat? : number
    lon? : number
    is_online? : boolean
    is_blocked? : boolean
    is_looking_someone_to_play_with?: boolean
    created_at: number
    last_profile_update: number
    last_post: number
    last_instrument_offer: number
    last_music_sheet: number
}


export const insertNewUser = (user: User) : string => {

    let keys = Object.keys(user).toString()
    let vlist = Object.values(user).toString()
    let av: string[] = vlist.split(',')
    for(let i = 0; i<av.length;i++) av[i] = `'${av[i]}'`
    let values = av.join(',')
    let sql = /*sql*/`
        INSERT INTO user (${keys}) VALUES (${values})        
        `
    return sql
}