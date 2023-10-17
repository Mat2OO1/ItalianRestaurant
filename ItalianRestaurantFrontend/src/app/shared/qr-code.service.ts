import {Injectable} from "@angular/core";
import {QRCodeComponent} from "angularx-qrcode";

@Injectable({
  providedIn: 'root',
})
export class QrCodeService {


  downloadQrCode(qrcode: QRCodeComponent) {
    let parentElement = qrcode.qrcElement.nativeElement.querySelector("img").src
    if (parentElement) {
      let blobData = this.convertBase64ToBlob(parentElement)
      const blob = new Blob([blobData], {type: "image/png"})
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement("a")
      link.href = url
      link.download = "table-qr-code"
      link.click()
    }
  }

  private convertBase64ToBlob(Base64Image: string) {
    const parts = Base64Image.split(";base64,")
    const imageType = parts[0].split(":")[1]
    const decodedData = window.atob(parts[1])
    const uInt8Array = new Uint8Array(decodedData.length)
    for (let i = 0; i < decodedData.length; ++i) {
      uInt8Array[i] = decodedData.charCodeAt(i)
    }
    return new Blob([uInt8Array], {type: imageType})
  }
}
