package com.example.lenovog50.pedometre2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogUtils {
    public static Dialog buildDialog(Context context, String title, String msg, String positiveButton, final DialogInterface.OnClickListener clickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clickListener.onClick(dialog,id);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }

    public static Dialog buildClickableInfoDialog(Context context,String title, String msg,String positiveButton,final DialogInterface.OnClickListener clickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clickListener.onClick(dialog,id);
            }
        });

        return builder.create();

    }

    public static Dialog buildClickableDialog(Context context, String title, String msg, String positiveButton, final DialogInterface.OnClickListener clickListener,
                                              final DialogInterface.OnClickListener onNegativeBuuttonClick)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clickListener.onClick(dialog,id);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onNegativeBuuttonClick.onClick(dialogInterface,i);
            }
        });

        return builder.create();

    }

    public static Dialog buildInfoDialog(Context context,String title, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();

    }

    public static Dialog buildProgressDialog (String msg, Context context)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        return dialog;
    }

    interface OnPositiveButtonClickListener{
        void OnClick();
    }
}

