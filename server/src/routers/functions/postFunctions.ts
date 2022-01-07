import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection";
import { selectPostPicture } from "../../database/sql/selectUser";
import { insertNewPost } from "../../database/sql/insertUser";
import Post from "../../@types/post";
import { OkPacket } from "mysql";
import { deletePost } from "../../database/sql/delete";

type BodyUpdate = {id:Number,author:string,title:string,description:string}

export const getPostImgFromId : ExpressRouterCallback = (req,res) => {
    const id = parseInt(req.params.id)

    if(isNaN(id)) res.send('err')
    else{
        const sql = selectPostPicture(id)
        DB.query(sql,(err,result) => {
            if(err)res.send(err)
            else{
                res.writeHead(200, {
                    'Content-Type': 'image/png',
                    'Content-Length': (result[0].content as Buffer).length
                });
                res.end(result[0].content as Buffer)
            }
        })
    }
}

export const loadNewPost: ExpressRouterCallback = (req,res) => {
    //console.log({...req.body,content:(req.body.content as String).length})

    console.log(typeof(req.body.hashtag as Array<String>))
    console.log(typeof(req.body.tag as Array<String>))
    console.log((req.body.hashtag as Array<String>))
    console.log((req.body.tag as Array<String>))

    const sql = insertNewPost(req.body as Post)
    const b = Buffer.from((req.body as Post).content,'base64')
    DB.query(sql,b,(err,result) => {
        if(err)res.status(500).send(err.message)
        else {
            res.json({'id':(result as OkPacket).insertId})
            DB.query(/*sql*/`UPDATE user SET last_post = CURRENT_TIMESTAMP WHERE id = ${(req.body as Post).posted_by};`);
            (req.body.hashtag as [string]).forEach((e:string) => {
                DB.query(/*sql*/`INSERT INTO hashtag (hashtag_name,hashtag_in) VALUES ('${e}',${(result as OkPacket).insertId})`) 
            });
            (req.body.tag as [string]).forEach((e:string) => {
                DB.query(/*sql*/`SELECT id FROM user WHERE username = '${e}'`, (err,ids) => {
                    if(ids.length === 1)DB.query(/*sql*/`INSERT INTO tag (tagged,tagged_in) VALUES ('${ids[0].id}',${(result as OkPacket).insertId})`) 
                })
            });
        }
    })
}

export const deleteUserPost: ExpressRouterCallback = (req,res) => {
    const id = parseInt(req.body.id)
    if(isNaN(id)) res.status(500).send({'error':'missing id'})

    else{
        DB.query(deletePost(id),(err) => {
            if(err) res.status(500).json({'error':'error'})
            else res.status(200).json(id)
        })
    }
}

export const updatePost: ExpressRouterCallback = (req,res) => {

    const {id,author,title,description} : BodyUpdate = req.body

    let sql = /*sql*/ `UPDATE posts SET author = '${author}',title = '${title}', description = '${description}',last_update_at = CURRENT_TIMESTAMP WHERE id = ${id};`

    DB.query(sql,(err) => {
        if(err)res.status(500).json({'error':err.message})
        else res.status(200).json({'good':'ok'})
    })

}