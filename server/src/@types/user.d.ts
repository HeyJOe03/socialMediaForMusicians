export default interface User{
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
    created_at?: Date
    last_profile_update?: Date
    last_post?: Date
    last_instrument_offer?: Date
    last_music_sheet?: Date
}