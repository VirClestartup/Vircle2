package id.kharisma.studio.vircle.Model

class Community {
    private var communityName: String = ""
    private var Deskripsi: String = ""
    private var Alasan: String = ""
    private var communityImage: String = ""
    private var communityId: String = ""
    private var publisher: String = ""

    constructor()
    constructor(Name: String,Deskripsi: String,Alasan: String,Communityimage: String,Communityid : String, publisher: String){
        this.communityName = Name
        this.Deskripsi = Deskripsi
        this.Alasan = Alasan
        this.communityImage = Communityimage
        this.communityId = Communityid
        this.publisher = publisher
    }
    fun getCommunityName(): String
    {
        return communityName
    }
    fun setCommunityName(Name: String)
    {
        this.communityName = Name
    }
    fun getDeskripsi(): String
    {
        return Deskripsi
    }
    fun setDeskripsi(Deskripsi: String)
    {
        this.Deskripsi = Deskripsi
    }
    fun getAlasan(): String
    {
        return Alasan
    }
    fun setAlasan(Alasan: String)
    {
        this.Alasan = Alasan
    }
    fun getcommunityImage(): String
    {
        return communityImage
    }
    fun setcommunityImage(Communityimage: String)
    {
        this.communityImage = Communityimage
    }
    fun getcommunityId(): String
    {
        return communityId
    }
    fun setcommunityId(Communityid: String)
    {
        this.communityId = Communityid
    }
    fun getpublisher(): String
    {
        return publisher
    }
    fun setpublisher(publisher: String)
    {
        this.publisher = publisher
    }
}