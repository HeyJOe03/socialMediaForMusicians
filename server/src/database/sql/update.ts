import Profile from "../../@types/profile";

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