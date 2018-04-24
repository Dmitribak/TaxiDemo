package dmitribak.taxi.taxidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class InnApp extends AppCompatActivity {

    private EditText InnApp_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.InApp_title);
        setContentView(R.layout.activity_inn_app);
//TODO: Настроить перелистывание экранов вправо или влево

///////////////////////////////////////////////////////////////
//Ввод номера телефона
        init(R.id.InnApp_phone, new ExampleBehaviour() {
            @Override
            void changeMask(MaskImpl mask) {
                mask.setHideHardcodedHead(true);
            }
        });
///////////////////////////////////////////////////////////////
    }





    public void forgot_password(View view) {
        TextView forgot_butt = (TextView)findViewById(R.id.InApp_forgot_password);
        Intent intent = new Intent(InnApp.this, Forgot_password.class);
        startActivity(intent);
    }

    public void registration(View view) {
        TextView registration_butt = (TextView)findViewById(R.id.InApp_registration);
        Intent intent = new Intent(InnApp.this, Registration.class);
        startActivity(intent);
    }

    public void logInn(View view) {
        //TODO: Авторизация в приложении

    }





///////////////////////////////////////////////////////////////
//Ввод номера телефона
    public void init(int editTextId, ExampleBehaviour behaviour) {
        EditText editText = (EditText) findViewById(editTextId);
        Slot[] slots = Slot.copySlotArray(PredefinedSlots.RUS_PHONE_NUMBER);
        behaviour.changeSlots(slots);
        MaskImpl mask = new MaskImpl(slots, behaviour.isTerminated());
        behaviour.changeMask(mask);
        MaskFormatWatcher watcher = new MaskFormatWatcher(mask);
        if (behaviour.fillWhenInstall()) {
            watcher.installOnAndFill(editText);
        } else {
            watcher.installOn(editText);
        }
    }
    class ExampleBehaviour {
        void changeMask(MaskImpl mask) {
        }

        void changeSlots(Slot[] slots) {
        }

        boolean isTerminated() {
            return true;
        }

        boolean fillWhenInstall() {
            return false;
        }
    }
//Ввод номера телефона
///////////////////////////////////////////////////////////////
}
