import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.Map;

public class MessageSenderImplTest {
    @Test
    void MessageSenderTest() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        Mockito.when(geoService.byIp("172.0.32.11")).thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(geoService.byIp("96.44.183.149")).thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        Assertions.assertEquals("Добро пожаловать", messageSender.send(Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11")));
        Assertions.assertEquals("Welcome", messageSender.send(Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149")));
    }

    @Test
    void GeoServiceImplTest() {
        GeoService geoService = new GeoServiceImpl();
        Assertions.assertSame(geoService.byIp("172.0.32.11").getCountry(), Country.RUSSIA);
        Assertions.assertSame(geoService.byIp("96.44.183.149").getCountry(), Country.USA);
    }

    @Test
    void LocalizationServiceImplTest() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        Assertions.assertEquals(localizationService.locale(Country.RUSSIA), "Добро пожаловать");
        Assertions.assertEquals(localizationService.locale(Country.USA), "Welcome");
    }
}
