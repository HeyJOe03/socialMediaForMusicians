export default interface Post{
    id? : number
    author : string
    title : string
    description : string
    content: string
    posted_by: Number
    likes? : Number
    created_at? : Date
    last_update_at? : Date
}