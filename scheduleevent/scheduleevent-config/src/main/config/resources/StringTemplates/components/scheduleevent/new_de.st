<div style="font-family:Arial;font-size:11px">
<b>$senderName$</b> m�chte, dass Sie an dem Event teilnehmen <b>$eventName$</b>.
For that, thank you to communicate your availability as soon as possible by clicking the attached link.
Daf�r danken wir Ihnen, Ihre Verf�gbarkeit so schnell wie m�glich zu kommunizieren, indem Sie den beigef�gten Link anklicken.

$if(notification_receiver_users)$
<i>Diese Nachricht wurde gesendet  nutzer : $notification_receiver_users$ </i>
$endif$

$if(notification_receiver_groups)$
  $if(!notification_receiver_users)$
  Diese Nachricht wurde gesendet
  $endif$
 gruppen : $notification_receiver_groups$
$endif$
</div>