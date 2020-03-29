package abolfazli.mahdi.weather.weather.data

import java.io.File


class TestUtil {
    companion object {
        fun getJson(path: String): String {
            val uri = this.javaClass.classLoader?.getResource(path)
            val file = File(uri?.path)
            return String(file.readBytes())
        }
    }
}