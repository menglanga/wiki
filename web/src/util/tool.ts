export class Tool{
    /**
     * 空校验null或“”都返回true
     */
    public static isEmpty(obj:any){
        if ((typeof obj == 'string')) {
            return !obj || obj.replace(/\s+/g,"") === ""
        }else{
            return (!obj || JSON.stringify(obj)==="{}" || obj.length===0);
        }
    }

    public static isNotEmpty(obj:any){
        return !this.isEmpty(obj);
    }

    public static copy(obj:object){
        if (Tool.isNotEmpty(obj)){
            return JSON.parse(JSON.stringify(obj));
        }
    }


    /**
     * 使用递归将数组转化为树形结构，父id属性为parent
     */

    public static array2tree(array:any, parentId:number){
        if (Tool.isEmpty(array)){
            return [];
        }
        const result=[];
        for (let i=0; i<array.length;i++){
            const c=array[i];
                if (Number(c.parent) === Number(parentId)){
                    result.push(c);
                    const children=Tool.array2tree(array,c.id);
                        if (Tool.isNotEmpty(children)){
                            c.children=children;
                        }
                }
            }
        return result;
    }

    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid (len: number, radix = 62) {
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');
    }



}