package ru.spb.tacticul.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.mapper.PartnerMapper;
import ru.spb.tacticul.model.Partner;
import ru.spb.tacticul.repository.PartnerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private PartnerMapper partnerMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private PartnerService partnerService;

    private Partner partner;
    private PartnerDTO partnerDTO;

    @BeforeEach
    void setUp() {
        partner = new Partner();
        partner.setId(1L);
        partner.setName("Test Partner");
        partner.setDescription("Test Description");

        partnerDTO = new PartnerDTO(1L, "Test Partner", "Test Description", null, "https://partner.com");
    }

    @Test
    void getAll_ShouldReturnListOfPartnerDTOs() {
        when(partnerRepository.findAll()).thenReturn(List.of(partner));
        when(partnerMapper.partnerToPartnerDTO(partner)).thenReturn(partnerDTO);

        List<PartnerDTO> result = partnerService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Partner", result.get(0).name());
        verify(partnerRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnPartnerDTO() {
        when(partnerRepository.findById(1L)).thenReturn(Optional.of(partner));
        when(partnerMapper.partnerToPartnerDTO(partner)).thenReturn(partnerDTO);

        PartnerDTO result = partnerService.getById(1L);

        assertEquals("Test Partner", result.name());
        verify(partnerRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(partnerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> partnerService.getById(2L));
    }

    @Test
    void create_ShouldReturnCreatedPartnerDTO() {
        when(partnerMapper.partnerDTOToPartner(partnerDTO)).thenReturn(partner);
        when(partnerRepository.save(partner)).thenReturn(partner);
        when(partnerMapper.partnerToPartnerDTO(partner)).thenReturn(partnerDTO);

        PartnerDTO result = partnerService.create(partnerDTO);

        assertEquals("Test Partner", result.name());
        verify(partnerRepository, times(1)).save(partner);
    }

    @Test
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(partnerRepository.findById(1L)).thenReturn(Optional.of(partner));
        when(partnerRepository.save(any())).thenReturn(partner);
        when(partnerMapper.partnerToPartnerDTO(any())).thenReturn(partnerDTO);

        PartnerDTO result = partnerService.update(1L, partnerDTO);

        assertEquals("Test Partner", result.name());
        verify(partnerRepository, times(1)).save(partner);
    }

    @Test
    void update_NonExistingId_ShouldThrowException() {
        when(partnerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> partnerService.update(2L, partnerDTO));
    }

    @Test
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(partnerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(partnerRepository).deleteById(1L);

        assertDoesNotThrow(() -> partnerService.delete(1L));
        verify(partnerRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldThrowException() {
        when(partnerRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> partnerService.delete(2L));
    }
}