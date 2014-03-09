package org.celllife.ivr.interfaces.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class FileUploadController {

    @Value('${external.base.url}')
    def String baseUrl;

    @RequestMapping("/")
    def index(Model model) {

        return ("index")

    }

    /*@RequestMapping(value = "/contactsCsvUpload", method = RequestMethod.POST)
    def save(@RequestParam("file") MultipartFile uploadedFile, @RequestParam Integer campaignId){

        def client = new org.celllife.ivr.framework.restclient.RESTClient()

        def query = [file: uploadedFile.getInputStream().getText()]

        try {
            client.post(baseUrl + "/service/campaign/" + campaignId + "/contacts", query)
            //return ("index")
        } catch (Exception e) {
            //return("error/customError")
        }

    }     */

}

