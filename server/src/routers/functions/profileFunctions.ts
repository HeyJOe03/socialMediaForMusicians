import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfile, selectProfilePicture} from "../../database/sql/selectUser"

export const profileFromID:ExpressRouterCallback = (req,res) => {
    if(!req.body.id)res.status(500)
    else{
        let id = -1
        if(typeof(req.body.id) != 'number'){
            try{
                id = parseInt(req.body.id)
            } catch(e){
                res.status(500)
                return
            }
        } else id = req.body.id
        let sql = selectProfile(id)
        DB.query(sql, (err,result) => {
            if(err) res.status(500)
            else{
                //console.log(((result[0].profile_image as Buffer).length))
                //const image = Buffer.from(result[0].profile_image).toString('base64')
                //res.json({...result[0],profile_image:image})
   

                res.json(result[0])

                // const image = Buffer.from((result[0].profile_image as Buffer).toJSON().data).toString('base64')
                // res.json({...result[0], profile_image: 'data:image/jpeg;base64,' + image})cdse

                // res.json(result[0])
            }
        })
    }
}

export const profileImageFromID:ExpressRouterCallback =(req,res) => {
    /*
    function decodeBase64Image(dataString: String): Buffer {
        let matches: RegExpMatchArray | null = dataString.match(/^data:([A-Za-z-+/]+);base64,(.+)$/)
        let  response: {type?:any,data?:any} = {}
      
        if (matches?.length !== 3) {
          throw new Error('Invalid input string')
        }
      
        response.type = matches[1];
        response.data = Buffer.from(matches[2], 'base64')
      
        return response.data;
      }
      
      const imageBuffer: Buffer = decodeBase64Image(data);
      console.log(imageBuffer);

      //res.send(imageBuffer)
      */
     const id = parseInt(req.params.id)
     //console.log(req.params)

      const sql = selectProfilePicture(id)
      DB.query(sql, (err,result) => {
        //console.log(result)
        res.writeHead(200, {
            'Content-Type': 'image/png',
            'Content-Length': (result[0].profile_image as Buffer).length
          });
          res.end(result[0].profile_image as Buffer)
      })

}