package timecard.input.camera

import java.awt.image.BufferedImage
import java.util.Hashtable

import com.google.zxing.{Reader, BinaryBitmap, DecodeHintType}
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.qrcode.QRCodeReader

class QRCode {
  val reader = new QRCodeReader

  def read(image: BufferedImage): String = {
    val source = new BufferedImageLuminanceSource(image)
    val bitmap = new BinaryBitmap(new HybridBinarizer(source))
    val hints = new Hashtable[DecodeHintType, Object]
    hints.put(DecodeHintType.TRY_HARDER, java.lang.Boolean.TRUE)
    try {
      val result = reader.decode(bitmap, hints)
      result.getText
    } catch {
      case _ => null
    }
  }
}
