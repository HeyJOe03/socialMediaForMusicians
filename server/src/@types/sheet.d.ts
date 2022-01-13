export default interface Sheet{
    id? : number
    author : string
    title : string
    description : string
    posted_by: Number
    content? : string
    likes? : Number
    created_at? : Date
    last_update_at? : Date
}