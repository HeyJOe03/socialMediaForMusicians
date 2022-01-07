export default interface  Profile{
    id: number,
    username: string, //max 20 char
    email: string,
    hash_password? : string // 30 char
    is_online? : boolean,
    is_blocked? : boolean,
    is_looking_someone_to_play_with : boolean,
    name: string,       
    lat: number?,
    lon: number?,
    description: string,
    instrument_interested_in: string,
    profile_pic: string,
    created_at?: number,
    last_post?: number,
    last_instrument_offer?: number,
    last_music_sheet?: number
}