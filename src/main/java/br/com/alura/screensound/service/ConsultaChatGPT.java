package br.com.alura.screensound.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ConsultaChatGPT {

    public static String obterInformacao(String texto) {
        try {
            String query = URLEncoder.encode(texto, "UTF-8");
            String apiUrl = "https://pt.wikipedia.org/api/rest_v1/page/summary/" + query;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return "Não foi possível encontrar informações sobre \"" + texto + "\".";
            }

            InputStream inputStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            WikipediaResposta resposta = mapper.readValue(inputStream, WikipediaResposta.class);

            if (resposta.getExtract() == null || resposta.getExtract().isBlank()) {
                return "Informação não disponível para o artista informado.";
            }

            return resposta.getExtract();

        } catch (Exception e) {
            return "Erro ao buscar informações: " + e.getMessage();
        }
    }
}
