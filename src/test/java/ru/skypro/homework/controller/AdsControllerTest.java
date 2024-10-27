package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AdService adService;
    @InjectMocks
    private AdsController adsController;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adsController).build();
    }
    @Test
    public void testFindAllAds() throws Exception {
        AdsDto adsDto = new AdsDto();
        when(adService.getAllAds()).thenReturn(adsDto);
        mockMvc.perform(get("/ads")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(adService, times(1)).getAllAds();
        verifyNoMoreInteractions(adService);
    }
    @Test
    public void testCreateAd() throws Exception {
        AdDto adDto = new AdDto();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image content".getBytes());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user");
        when(adService.saveAd(any(AdDto.class), any(MultipartFile.class), anyString())).thenReturn(adDto);
        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Ad\"}")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(adService, times(1)).saveAd(any(AdDto.class), any(MultipartFile.class), anyString());
        verifyNoMoreInteractions(adService);
    }
    @Test
    public void testGetAdInfo() throws Exception {
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();
        when(adService.getAdInfo(1)).thenReturn(extendedAdDto);
        mockMvc.perform(get("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(adService, times(1)).getAdInfo(1);
        verifyNoMoreInteractions(adService);
    }
    @Test
    public void testDeleteAd() throws Exception {
        mockMvc.perform(delete("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(adService, times(1)).deleteAd(1);
        verifyNoMoreInteractions(adService);
    }
    @Test
    public void testFindMyAds() throws Exception {
        AdsDto adsDto = new AdsDto();
        when(adService.findMyAds()).thenReturn(adsDto);
        mockMvc.perform(get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(adService, times(1)).findMyAds();
        verifyNoMoreInteractions(adService);
    }
}
