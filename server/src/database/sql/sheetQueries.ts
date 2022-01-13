import Sheet from "../../@types/sheet"

export const selectSheetsIDQuery = (id: number) : string => {
    return /*sql*/`SELECT id FROM musicsheet WHERE posted_by = ${id}`
}

export const selectSheetInfoQuery = (id: number) : string => {
    return /*sql*/`SELECT id,description,posted_by,created_at,last_update_at,author,title,likes FROM musicsheet WHERE id = ${id}`
}

export const selectSheetPictureQuery = (id: number) : string => {
    return /*sql*/`SELECT sheet FROM musicsheet WHERE id = ${id}`
}

export const insertNewPostQuery = (sheet: Sheet) : string => {
    let sql = /*sql*/`
        INSERT INTO posts (author,title,description,posted_by,sheet) 
        VALUES ('${sheet.author}','${sheet.title}','${sheet.description}',${sheet.posted_by},?)`

    return sql
}

export const deleteSheetQuery = (id: Number): string => {
    return /*sql*/`DELETE FROM musicsheet WHERE id = ${id}`
}

export const updateSheetQuery = (id: Number,author:string,title:string,description:string): string => {
    return /*sql*/ `UPDATE musicsheet SET author = '${author}',title = '${title}', description = '${description}',last_update_at = CURRENT_TIMESTAMP WHERE id = ${id};`
}