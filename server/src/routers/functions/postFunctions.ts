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
    const sql = insertNewPost(req.body as Post)
    const b = Buffer.from((req.body as Post).content,'base64')
    DB.query(sql,b,(err,result) => {
        if(err)res.json({'error':err.message})
        else res.json({'ok':'ok','id':(result as OkPacket).insertId})
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

    let sql = /*sql*/ `UPDATE posts SET author = '${author}',title = '${title}', description = '${description}' WHERE id = ${id};`

    DB.query(sql,(err) => {
        if(err)res.status(500).json({'error':err.message})
        else res.status(200).json({'good':'ok'})
    })

}