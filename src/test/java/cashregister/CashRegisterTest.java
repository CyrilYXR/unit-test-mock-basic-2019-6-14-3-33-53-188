package cashregister;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.*;

public class CashRegisterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    public void should_print_the_real_purchase_when_call_process() {
        //given
        Printer printer = new Printer();
        CashRegister cashRegister = new CashRegister(printer);
        Purchase purchase = new Purchase(new Item[]{
                new Item("item1", 1),
                new Item("item2", 2),
                new Item("item3", 3)

        });
        //when
        cashRegister.process(purchase);
        //then
        assertThat(outContent.toString()).isEqualTo("item1\t1.0\nitem2\t2.0\nitem3\t3.0\n");

    }

    @Test
    public void should_print_the_stub_purchase_when_call_process() {
        //given
        CashRegister cashRegister = new CashRegister(new Printer(){
            @Override
            public void print(String printThis) {
                System.out.print("stub:" + printThis);
            }
        });
        Purchase purchase = new Purchase(new Item[]{
                new Item("item1", 1),
                new Item("item2", 2),
                new Item("item3", 3)

        });
        //when
        cashRegister.process(purchase);
        //then
        assertThat(outContent.toString()).isEqualTo("stub:item1\t1.0\nitem2\t2.0\nitem3\t3.0\n");

    }

    @Test
    public void should_verify_with_process_call_with_mockito() {
        //given
        Printer printer = mock(Printer.class);
        CashRegister cashRegister = new CashRegister(printer);
        Purchase purchase = new Purchase(new Item[]{
                new Item("item1", 1),
                new Item("item2", 2),
                new Item("item3", 3)

        });
        //when
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        cashRegister.process(purchase);
        //then
        verify(printer).print("item1\t1.0\nitem2\t2.0\nitem3\t3.0\n");
        verify(printer).print(argumentCaptor.capture());
        Assertions.assertEquals("item1\t1.0\nitem2\t2.0\nitem3\t3.0\n", argumentCaptor.getValue());
    }

}
