package ee.webmedia.xtee.client.teavituskalender;

import ee.webmedia.xtee.client.exception.XTeeServiceConsumptionException;
import ee.webmedia.xtee.client.service.XTeeDatabaseService;
import ee.webmedia.xtee.client.teavituskalender.database.TeavituskalenderXTeeDatabase;
import ee.webmedia.xtee.client.teavituskalender.types.ee.riik.xtee.teavituskalender.producers.producer.teavituskalender.LisaSyndmusParing;
import ee.webmedia.xtee.client.teavituskalender.types.ee.riik.xtee.teavituskalender.producers.producer.teavituskalender.LisaSyndmusParing.Lugejad;
import ee.webmedia.xtee.client.teavituskalender.types.ee.riik.xtee.teavituskalender.producers.producer.teavituskalender.LisaSyndmusParing.Lugejad.Item;
import ee.webmedia.xtee.client.teavituskalender.types.ee.riik.xtee.teavituskalender.producers.producer.teavituskalender.LisaSyndmusVastus;
import javax.annotation.Resource;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.stereotype.Service;

/**
 * @author Aleksandr.Koltakov
 */
@Service("teavituskalenderXTeeService")
public class TeavituskalenderXTeeServiceImpl extends XTeeDatabaseService implements TeavituskalenderXTeeService {

  private static final String TYPE = "xsd:anyType[1]";
  private static final String OFFSET = "[0]";

  @Resource
  private TeavituskalenderXTeeDatabase teavituskalenderXTeeDatabase;

  public LisaSyndmusVastus lisaSyndmus(Syndmus syndmus) throws XTeeServiceConsumptionException {

    LisaSyndmusParing paring = LisaSyndmusParing.Factory.newInstance();
    paring.setNahtavOmanikule(syndmus.isNahtavOmanikule());
    paring.setKirjeldus(syndmus.getKirjeldus());
    paring.setAegunudTeavitus(syndmus.getAegunudTeavitus());
    paring.setAktiivneTeavitus(syndmus.getAktivineTeavitus());
    paring.setTahtsus(syndmus.getTahtsus());
    paring.setSyndmuseTyyp(syndmus.getSyndmuseTyyp());
    paring.setLiik(syndmus.getLiik());

    paring.setAlgus(Parser.parseDateTime(syndmus.getAlgus()));
    paring.setLopp(Parser.parseDateTime(syndmus.getLopp()));
    paring.setTeavitusAlgus(Parser.parseDate(syndmus.getTeavitusAlgus()));
    paring.setTeavitusLopp(Parser.parseDate(syndmus.getTeavitusLopp()));
    paring.setAegumine(Parser.parseDate(syndmus.getAegumine()));

    Item kasutaja = Item.Factory.newInstance();

    kasutaja.setKasutajaTyyp(syndmus.getKasutajaTyyp());
    kasutaja.setKood(syndmus.getIsikukood());
    Item[] kasutajad = new Item[1];
    kasutajad[0] = kasutaja;

    XmlOptions options = new XmlOptions();
    Lugejad lugejad = Lugejad.Factory.newInstance(options);
    lugejad.addNewItem();
    lugejad.setArrayType(TYPE);
    lugejad.setOffset(OFFSET);
    lugejad.setItemArray(kasutajad);
    paring.setLugejad(lugejad);

    return teavituskalenderXTeeDatabase.lisaSyndmusV1(paring);
  }


  public void setTeavituskalenderXTeeDatabase(TeavituskalenderXTeeDatabase teavituskalenderXTeeDatabase) {
    this.teavituskalenderXTeeDatabase = teavituskalenderXTeeDatabase;
  }
}