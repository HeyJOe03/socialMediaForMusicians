import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectShopPictureQuery, selectShopInfoQuery,selectShopsIDQuery,insertNewShopQuery,deleteShopQuery, updateShopQuery } from "../../database/sql/shopQueries"
import Shop from "../../@types/shop"
import { OkPacket } from "mysql"

type BodyUpdate = {id:Number,price: Number,instrument_description:string}


export const getShopImgFromId : ExpressRouterCallback = (req,res) => {
    const sql = selectShopPictureQuery(parseInt(req.params.id))
    DB.query(sql,(err,result) => {
        if(err)res.status(500).send(err.message)
        else{
            try{
                res.writeHead(200, {
                    'Content-Type': 'image/png',
                    'Content-Length': (result[0].content as Buffer).length
                })
                res.end(result[0].content as Buffer)
            } catch (e){
                res.status(500).send("query error id doesn't exist")
            }
            
        }
    })
}

export const loadNewShop: ExpressRouterCallback = (req,res) => {
    const sql = insertNewShopQuery(req.body as Shop)
    const b = Buffer.from((req.body as Shop).content!!,'base64')
    DB.query(sql,b,(err,result) => {
        if(err) res.status(500).send(err.message)
        else {
            res.json({'id':(result as OkPacket).insertId})
            DB.query(/*sql*/`UPDATE user SET last_instrument_offer = CURRENT_TIMESTAMP WHERE id = ${(req.body as Shop).posted_by};`);
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

export const deleteUserShop: ExpressRouterCallback = (req,res) => {
    const id = parseInt(req.body.id)
    if(isNaN(id)) res.status(500).send({'error':'missing id'})

    else{
        const sql = deleteShopQuery(id)
        DB.query(sql,(err) => {
            if(err) res.status(500).json({'error':'error'})
            else res.status(200).json(id)
        })
    }
}

export const userShops: ExpressRouterCallback = (req,res) => {

    if(req.body.id == undefined || isNaN(parseInt(req.body.id))) res.status(500).send('id not provided')

    else{
        let id = parseInt(req.body.id)
        const sql = selectShopsIDQuery(id)
        DB.query(sql,(err,result) => {
            if(err) res.status(500).send(err.message)
            else res.json({"result":result})   
        })
    }

}

export const updateShop: ExpressRouterCallback = (req,res) => {

    const {id,price,instrument_description} : BodyUpdate = req.body

    const sql = updateShopQuery(id,price,instrument_description)
    DB.query(sql,(err) => {
        if(err)res.status(500).json({'error':err.message})
        else res.status(200).json({'good':'ok'})
    })

}

export const infoShop: ExpressRouterCallback = (req,res) => {
    const id = req.body.id
    const sql = selectShopInfoQuery(id)
    DB.query(sql,(err,result) => {
        if(err)res.status(500).send(err.message)
        else res.status(200).json(result[0])
    })
}