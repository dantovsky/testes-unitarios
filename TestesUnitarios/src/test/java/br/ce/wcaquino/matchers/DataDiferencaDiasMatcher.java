package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DataDiferencaDiasMatcher
        extends TypeSafeMatcher<Date> {

    private Integer qtdDias;

    public DataDiferencaDiasMatcher(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    public void describeTo(Description description) { }

    @Override protected boolean matchesSafely(Date date) {
        System.out.println("Data recebida: " + date);
        return DataUtils.isMesmaData(date, DataUtils.obterDataComDiferencaDias(qtdDias));
    }
}
