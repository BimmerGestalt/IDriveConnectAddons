package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import io.bimmergestalt.idriveconnectkit.android.CarAppResources
import io.bimmergestalt.idriveconnectkit.android.CertMangling
import io.bimmergestalt.idriveconnectkit.android.security.SecurityAccess
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * An implementation of CarAppResources for easy use in Android apps
 *
 * Loads resource files from the app context's assets
 *
 * It expects to find the App Certificate at:
 *   - carapplications/$name/rhmi/$brand/$name.p7b
 *   - carapplications/$name/$name.p7b
 *
 * It expects to find the ui_description.xml at:
 *   - carapplications/$name/rhmi/$brand/ui_description.xml
 *   - carapplications/$name/rhmi/common/ui_description.xml
 *   - carapplications/$name/rhmi/ui_description.xml
 *
 * It expects to find the images.zip file at:
 *   - carapplications/$name/rhmi/$brand/images.zip
 *   - carapplications/$name/rhmi/common/images.zip
 *
 * It expects to find the textx.zip file at:
 *   - carapplications/$name/rhmi/$brand/texts.zip
 *   - carapplications/$name/rhmi/common/texts.zip
 *
 * The $brand is always lower-cased
 *
 * This module uses SecurityAccess to automatically alter the App Certificate
 * to include the necessary extra certs from the Security Module.
 *
 * todo: Move to IDriveConnectKitAndroid
 * */
class CarAppAssetResources(val context: Context, val name: String): CarAppResources {
    fun loadFile(path: String): InputStream? {
        try {
            return context.assets.open(path)
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    fun getAppCertificateRaw(brand: String): InputStream? {
        return loadFile("carapplications/$name/rhmi/${brand.toLowerCase()}/$name.p7b") ?:
        loadFile("carapplications/$name/$name.p7b")
    }

    override fun getAppCertificate(brand: String): InputStream? {
        val appCert = getAppCertificateRaw(brand)?.readBytes() as ByteArray
        val signedCert = CertMangling.mergeBMWCert(appCert, SecurityAccess.getInstance(context).fetchBMWCerts(brandHint= brand))
        return ByteArrayInputStream(signedCert)
    }

    override fun getUiDescription(brand: String): InputStream? {
        return loadFile("carapplications/$name/rhmi/${brand.toLowerCase()}/ui_description.xml") ?:
        loadFile("carapplications/$name/rhmi/common/ui_description.xml") ?:
        loadFile("carapplications/$name/rhmi/ui_description.xml")
    }

    override fun getImagesDB(brand: String): InputStream? {
        return loadFile("carapplications/$name/rhmi/${brand.toLowerCase()}/images.zip") ?:
        loadFile("carapplications/$name/rhmi/common/images.zip")
    }

    override fun getTextsDB(brand: String): InputStream? {
        return loadFile("carapplications/$name/rhmi/${brand.toLowerCase()}/texts.zip") ?:
        loadFile("carapplications/$name/rhmi/common/texts.zip")
    }
}