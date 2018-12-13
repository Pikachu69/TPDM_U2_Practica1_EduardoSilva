package mx.edu.ittepic.tpdm_u2_practica1_edaurdosilva;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText id,dias,desc,cal;
    Button insert,update,delete,find;
    BaseDatos base;
    TextView datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        base = new BaseDatos(this,"baseRutina",null,1);

        id = findViewById(R.id.id);
        dias = findViewById(R.id.dias);
        desc = findViewById(R.id.descripcion);
        cal = findViewById(R.id.calorias);
        datos = findViewById(R.id.datos);
        insert = findViewById(R.id.insertar);
        update = findViewById(R.id.actualizar);
        delete = findViewById(R.id.eliminar);
        find = findViewById(R.id.buscar);

        buscar();

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregar();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPorId();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrar();
            }
        });
    }

    private void borrar() {
        String ide = id.getText().toString();
        SQLiteDatabase bd = base.getWritableDatabase();
        try {
            bd.delete("rutina","id = "+ide,null);
        } catch (SQLException e) {
            mensaje("Error", e.getMessage());
        }
        bd.close();
        buscar();
        limpiarCampos();
    }

    private void buscarPorId() {
        String ide = id.getText().toString();
        SQLiteDatabase bd = base.getReadableDatabase();
        String[] clave = {ide};
        try {
            Cursor c = bd.rawQuery("SELECT * FROM rutina WHERE id = ?",clave);
            if (c.moveToFirst()) {
                dias.setText(c.getString(1));
                desc.setText(c.getString(2));
                cal.setText(c.getInt(3)+"");
            } else mensaje("Error", "No se encontro el dato");
            c.close();
        } catch (SQLException e) {
            mensaje("Error",e.getMessage());
        }
        bd.close();
    }

    private void buscar() {
        String dato = "Id\t\t\t\t\tDias\t\t\t\t\tDescripcion\t\t\t\tCalorias Quemadas\n\n";
        SQLiteDatabase bd = base.getReadableDatabase();
        try {
            Cursor c = bd.rawQuery("SELECT * FROM rutina",null);
            if (c.moveToFirst()) {
                do {
                    String consulta = + c.getInt(0) + "\t\t\t\t" + c.getString(1) + "\t\t\t\t\t\t" +
                            c.getString(2) + "\t\t\t\t\t\t\t\t\t\t\t\t" + c.getInt(3) + "\n";
                    dato = dato+consulta;
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            mensaje("Error",e.getMessage());
        }
        bd.close();
        datos.setText(dato);

    }

    private void agregar() {
        String dia = dias.getText().toString();
        String descr = desc.getText().toString();
        int calo = Integer.parseInt(cal.getText().toString());
        SQLiteDatabase bd = base.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("dias", dia);
        cv.put("descripcion", descr);
        cv.put("caloriasquemadas",calo);
        try {
            bd.insert("rutina",null,cv);
        } catch (SQLException e) {
            mensaje("Error",e.getMessage());
        }
        bd.close();
        limpiarCampos();
        buscar();
    }

    private void actualizar() {
        int ide = Integer.parseInt(id.getText().toString());
        String dia = dias.getText().toString();
        String descr = desc.getText().toString();
        int calo = Integer.parseInt(cal.getText().toString());

        SQLiteDatabase bd = base.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("dias", dia);
        cv.put("descripcion", descr);
        cv.put("caloriasquemadas", calo);
        try {
            bd.update("rutina",cv,"id = "+ide, null);
        } catch (SQLException e) {
            mensaje("Error", e.getMessage());
        }
        bd.close();
        buscar();
        limpiarCampos();
    }

    private void limpiarCampos() {
        id.setText("");
        dias.setText("");
        desc.setText("");
        cal.setText("");
    }

    private void mensaje(String title, String e) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(title).setMessage(e).show();
    }
}
