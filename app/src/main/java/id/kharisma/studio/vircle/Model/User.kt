package id.kharisma.studio.vircle.Model

class User {
    private var Username: String = ""
    private var Email: String = ""
    private var Password: String = ""
    private var Image: String = ""
    private var uid: String = ""


    constructor()
    constructor(Username: String,Email: String, Password: String, Image: String, uid: String)
    {
        this.Username = Username
        this.Email = Email
        this.Password = Password
        this.Image = Image
        this.uid = uid
    }

    fun getUsername(): String
    {
        return Username
    }
    fun setUsername(Username: String)
    {
        this.Username = Username
    }
    fun getEmail(): String
    {
        return Email
    }
    fun setEmail(Email: String)
    {
        this.Email = Email
    }
    fun getPassword(): String
    {
        return Password
    }
    fun setPassword(Password: String)
    {
        this.Password = Password
    }
    fun getImage(): String
    {
        return Image
    }
    fun setimage(Image: String)
    {
        this.Image = Image
    }
    fun getUID(): String
    {
        return uid
    }
    fun setUID(uid: String)
    {
        this.uid = uid
    }
}