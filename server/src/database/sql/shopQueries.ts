import Shop from "../../@types/shop"

export const selectShopsIDQuery = (id: number) : string => {
    return /*sql*/`SELECT id FROM secondhandinstruments WHERE posted_by = ${id}`
}

export const selectShopInfoQuery = (id: number) : string => {
    return /*sql*/`SELECT id,instrument_description,posted_by,created_at,last_update_at,likes FROM secondhandinstruments WHERE id = ${id}`
}

export const selectShopPictureQuery = (id: number) : string => {
    return /*sql*/`SELECT content FROM secondhandinstruments WHERE id = ${id}`
}

export const insertNewShopQuery = (shop: Shop) : string => {
    let sql = /*sql*/`
        INSERT INTO secondhandinstruments (price,instrument_description,posted_by,content) 
        VALUES (${shop.price},'${shop.instrument_description}',${shop.posted_by},?)`

    return sql
}

export const deleteShopQuery = (id: Number): string => {
    return /*sql*/`DELETE FROM secondhandinstruments WHERE id = ${id}`
}

export const updateShopQuery = (id: Number,price:Number,instrument_description:string): string => {
    return /*sql*/ `UPDATE secondhandinstruments SET price = ${price},instrument_description = '${instrument_description}',last_update_at = CURRENT_TIMESTAMP WHERE id = ${id};`
}