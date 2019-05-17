package org.lab.webcrawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class UtilFunction {
	public static String[] icons = { "3d-rotation", "ac-unit", "access-alarm", "access-alarms", "access-time",
			"accessibility", "accessible", "account-balance", "account-balance-wallet", "account-box", "account-circle",
			"adb", "add", "add-a-photo", "add-alarm", "add-alert", "add-box", "add-circle", "add-circle-outline",
			"add-location", "add-shopping-cart", "add-to-photos", "add-to-queue", "adjust", "airline-seat-flat",
			"airline-seat-flat-angled", "airline-seat-individual-suite", "airline-seat-legroom-extra",
			"airline-seat-legroom-normal", "airline-seat-legroom-reduced", "airline-seat-recline-extra",
			"airline-seat-recline-normal", "airplanemode-active", "airplanemode-inactive", "airplay", "airport-shuttle",
			"alarm", "alarm-add", "alarm-off", "alarm-on", "album", "all-inclusive", "all-out", "android",
			"announcement", "apps", "archive", "arrow-back", "arrow-downward", "arrow-drop-down",
			"arrow-drop-down-circle", "arrow-drop-up", "arrow-forward", "arrow-upward", "art-track", "aspect-ratio",
			"assessment", "assignment", "assignment-ind", "assignment-late", "assignment-return", "assignment-returned",
			"assignment-turned-in", "assistant", "assistant-photo", "attach-file", "attach-money", "attachment",
			"audiotrack", "autorenew", "av-timer", "backspace", "backup", "battery-alert", "battery-charging-full",
			"battery-full", "battery-std", "battery-unknown", "beach-access", "beenhere", "block", "bluetooth",
			"bluetooth-audio", "bluetooth-connected", "bluetooth-disabled", "bluetooth-searching", "blur-circular",
			"blur-linear", "blur-off", "blur-on", "book", "bookmark", "bookmark-border", "border-all", "border-bottom",
			"border-clear", "border-color", "border-horizontal", "border-inner", "border-left", "border-outer",
			"border-right", "border-style", "border-top", "border-vertical", "branding-watermark", "brightness-1",
			"brightness-2", "brightness-3", "brightness-4", "brightness-5", "brightness-6", "brightness-7",
			"brightness-auto", "brightness-high", "brightness-low", "brightness-medium", "broken-image", "brush",
			"bubble-chart", "bug-report", "build", "burst-mode", "business", "business-center", "cached", "cake",
			"call", "call-end", "call-made", "call-merge", "call-missed", "call-missed-outgoing", "call-received",
			"call-split", "call-to-action", "camera", "camera-alt", "camera-enhance", "camera-front", "camera-rear",
			"camera-roll", "cancel", "card-giftcard", "card-membership", "card-travel", "casino", "cast",
			"cast-connected", "center-focus-strong", "center-focus-weak", "change-history", "chat", "chat-bubble",
			"chat-bubble-outline", "check", "check-box", "check-box-outline-blank", "check-circle", "chevron-left",
			"chevron-right", "child-care", "child-friendly", "chrome-reader-mode", "class", "clear", "clear-all",
			"close", "closed-caption", "cloud", "cloud-circle", "cloud-done", "cloud-download", "cloud-off",
			"cloud-queue", "cloud-upload", "code", "collections", "collections-bookmark", "color-lens", "colorize",
			"comment", "compare", "compare-arrows", "computer", "confirmation-number", "contact-mail", "contact-phone",
			"contacts", "content-copy", "content-cut", "content-paste", "control-point", "control-point-duplicate",
			"copyright", "create", "create-new-folder", "credit-card", "crop", "crop-16-9", "crop-3-2", "crop-5-4",
			"crop-7-5", "crop-din", "crop-free", "crop-landscape", "crop-original", "crop-portrait", "crop-rotate",
			"crop-square", "dashboard", "data-usage", "date-range", "dehaze", "delete", "delete-forever",
			"delete-sweep", "description", "desktop-mac", "desktop-windows", "details", "developer-board",
			"developer-mode", "device-hub", "devices", "devices-other", "dialer-sip", "dialpad", "directions",
			"directions-bike", "directions-boat", "directions-bus", "directions-car", "directions-railway",
			"directions-run", "directions-subway", "directions-transit", "directions-walk", "disc-full", "dns",
			"do-not-disturb", "do-not-disturb-alt", "do-not-disturb-off", "do-not-disturb-on", "dock", "domain", "done",
			"done-all", "donut-large", "donut-small", "drafts", "drag-handle", "drive-eta", "dvr", "edit",
			"edit-location", "eject", "email", "enhanced-encryption", "equalizer", "error", "error-outline",
			"euro-symbol", "ev-station", "event", "event-available", "event-busy", "event-note", "event-seat",
			"exit-to-app", "expand-less", "expand-more", "explicit", "explore", "exposure", "exposure-neg-1",
			"exposure-neg-2", "exposure-plus-1", "exposure-plus-2", "exposure-zero", "extension", "face",
			"fast-forward", "fast-rewind", "favorite", "favorite-border", "featured-play-list", "featured-video",
			"feedback", "fiber-dvr", "fiber-manual-record", "fiber-new", "fiber-pin", "fiber-smart-record",
			"file-download", "file-upload", "filter", "filter-1", "filter-2", "filter-3", "filter-4", "filter-5",
			"filter-6", "filter-7", "filter-8", "filter-9", "filter-9-plus", "filter-b-and-w", "filter-center-focus",
			"filter-drama", "filter-frames", "filter-hdr", "filter-list", "filter-none", "filter-tilt-shift",
			"filter-vintage", "find-in-page", "find-replace", "fingerprint", "first-page", "fitness-center", "flag",
			"flare", "flash-auto", "flash-off", "flash-on", "flight", "flight-land", "flight-takeoff", "flip",
			"flip-to-back", "flip-to-front", "folder", "folder-open", "folder-shared", "folder-special",
			"font-download", "format-align-center", "format-align-justify", "format-align-left", "format-align-right",
			"format-bold", "format-clear", "format-color-fill", "format-color-reset", "format-color-text",
			"format-indent-decrease", "format-indent-increase", "format-italic", "format-line-spacing",
			"format-list-bulleted", "format-list-numbered", "format-paint", "format-quote", "format-shapes",
			"format-size", "format-strikethrough", "format-textdirection-l-to-r", "format-textdirection-r-to-l",
			"format-underlined", "forum", "forward", "forward-10", "forward-30", "forward-5", "free-breakfast",
			"fullscreen", "fullscreen-exit", "functions", "g-translate", "gamepad", "games", "gavel", "gesture",
			"get-app", "gif", "golf-course", "gps-fixed", "gps-not-fixed", "gps-off", "grade", "gradient", "grain",
			"graphic-eq", "grid-off", "grid-on", "group", "group-add", "group-work", "hd", "hdr-off", "hdr-on",
			"hdr-strong", "hdr-weak", "headset", "headset-mic", "healing", "hearing", "help", "help-outline",
			"high-quality", "highlight", "highlight-off", "history", "home", "hot-tub", "hotel", "hourglass-empty",
			"hourglass-full", "http", "https", "image", "image-aspect-ratio", "import-contacts", "import-export",
			"important-devices", "inbox", "indeterminate-check-box", "info", "info-outline", "input", "insert-chart",
			"insert-comment", "insert-drive-file", "insert-emoticon", "insert-invitation", "insert-link",
			"insert-photo", "invert-colors", "invert-colors-off", "iso", "keyboard", "keyboard-arrow-down",
			"keyboard-arrow-left", "keyboard-arrow-right", "keyboard-arrow-up", "keyboard-backspace",
			"keyboard-capslock", "keyboard-hide", "keyboard-return", "keyboard-tab", "keyboard-voice", "kitchen",
			"label", "label-outline", "landscape", "language", "laptop", "laptop-chromebook", "laptop-mac",
			"laptop-windows", "last-page", "launch", "layers", "layers-clear", "leak-add", "leak-remove", "lens",
			"library-add", "library-books", "library-music", "lightbulb-outline", "line-style", "line-weight",
			"linear-scale", "link", "linked-camera", "list", "live-help", "live-tv", "local-activity", "local-airport",
			"local-atm", "local-bar", "local-cafe", "local-car-wash", "local-convenience-store", "local-dining",
			"local-drink", "local-florist", "local-gas-station", "local-grocery-store", "local-hospital", "local-hotel",
			"local-laundry-service", "local-library", "local-mall", "local-movies", "local-offer", "local-parking",
			"local-pharmacy", "local-phone", "local-pizza", "local-play", "local-post-office", "local-printshop",
			"local-see", "local-shipping", "local-taxi", "location-city", "location-disabled", "location-off",
			"location-on", "location-searching", "lock", "lock-open", "lock-outline", "looks", "looks-3", "looks-4",
			"looks-5", "looks-6", "looks-one", "looks-two", "loop", "loupe", "low-priority", "loyalty", "mail",
			"mail-outline", "map", "markunread", "markunread-mailbox", "memory", "menu", "merge-type", "message", "mic",
			"mic-none", "mic-off", "mms", "mode-comment", "mode-edit", "monetization-on", "money-off",
			"monochrome-photos", "mood", "mood-bad", "more", "more-horiz", "more-vert", "motorcycle", "mouse",
			"move-to-inbox", "movie", "movie-creation", "movie-filter", "multiline-chart", "music-note", "music-video",
			"my-location", "nature", "nature-people", "navigate-before", "navigate-next", "navigation", "near-me",
			"network-cell", "network-check", "network-locked", "network-wifi", "new-releases", "next-week", "nfc",
			"no-encryption", "no-sim", "not-interested", "note", "note-add", "notifications", "notifications-active",
			"notifications-none", "notifications-off", "notifications-paused", "offline-pin", "ondemand-video",
			"opacity", "open-in-browser", "open-in-new", "open-with", "pages", "pageview", "palette", "pan-tool",
			"panorama", "panorama-fish-eye", "panorama-horizontal", "panorama-vertical", "panorama-wide-angle",
			"party-mode", "pause", "pause-circle-filled", "pause-circle-outline", "payment", "people", "people-outline",
			"perm-camera-mic", "perm-contact-calendar", "perm-data-setting", "perm-device-information", "perm-identity",
			"perm-media", "perm-phone-msg", "perm-scan-wifi", "person", "person-add", "person-outline", "person-pin",
			"person-pin-circle", "personal-video", "pets", "phone", "phone-android", "phone-bluetooth-speaker",
			"phone-forwarded", "phone-in-talk", "phone-iphone", "phone-locked", "phone-missed", "phone-paused",
			"phonelink", "phonelink-erase", "phonelink-lock", "phonelink-off", "phonelink-ring", "phonelink-setup",
			"photo", "photo-album", "photo-camera", "photo-filter", "photo-library", "photo-size-select-actual",
			"photo-size-select-large", "photo-size-select-small", "picture-as-pdf", "picture-in-picture",
			"picture-in-picture-alt", "pie-chart", "pie-chart-outlined", "pin-drop", "place", "play-arrow",
			"play-circle-filled", "play-circle-outline", "play-for-work", "playlist-add", "playlist-add-check",
			"playlist-play", "plus-one", "poll", "polymer", "pool", "portable-wifi-off", "portrait", "power",
			"power-input", "power-settings-new", "pregnant-woman", "present-to-all", "print", "priority-high", "public",
			"publish", "query-builder", "question-answer", "queue", "queue-music", "queue-play-next", "radio",
			"radio-button-checked", "radio-button-unchecked", "rate-review", "receipt", "recent-actors",
			"record-voice-over", "redeem", "redo", "refresh", "remove", "remove-circle", "remove-circle-outline",
			"remove-from-queue", "remove-red-eye", "remove-shopping-cart", "reorder", "repeat", "repeat-one", "replay",
			"replay-10", "replay-30", "replay-5", "reply", "reply-all", "report", "report-problem", "restaurant",
			"restaurant-menu", "restore", "restore-page", "ring-volume", "room", "room-service",
			"rotate-90-degrees-ccw", "rotate-left", "rotate-right", "rounded-corner", "router", "rowing", "rss-feed",
			"rv-hookup", "satellite", "save", "scanner", "schedule", "school", "screen-lock-landscape",
			"screen-lock-portrait", "screen-lock-rotation", "screen-rotation", "screen-share", "sd-card", "sd-storage",
			"search", "security", "select-all", "send", "sentiment-dissatisfied", "sentiment-neutral",
			"sentiment-satisfied", "sentiment-very-dissatisfied", "sentiment-very-satisfied", "settings",
			"settings-applications", "settings-backup-restore", "settings-bluetooth", "settings-brightness",
			"settings-cell", "settings-ethernet", "settings-input-antenna", "settings-input-component",
			"settings-input-composite", "settings-input-hdmi", "settings-input-svideo", "settings-overscan",
			"settings-phone", "settings-power", "settings-remote", "settings-system-daydream", "settings-voice",
			"share", "shop", "shop-two", "shopping-basket", "shopping-cart", "short-text", "show-chart", "shuffle",
			"signal-cellular-4-bar", "signal-cellular-connected-no-internet-4-bar", "signal-cellular-no-sim",
			"signal-cellular-null", "signal-cellular-off", "signal-wifi-4-bar", "signal-wifi-4-bar-lock",
			"signal-wifi-off", "sim-card", "sim-card-alert", "skip-next", "skip-previous", "slideshow",
			"slow-motion-video", "smartphone", "smoke-free", "smoking-rooms", "sms", "sms-failed", "snooze", "sort",
			"sort-by-alpha", "spa", "space-bar", "speaker", "speaker-group", "speaker-notes", "speaker-notes-off",
			"speaker-phone", "spellcheck", "star", "star-border", "star-half", "stars", "stay-current-landscape",
			"stay-current-portrait", "stay-primary-landscape", "stay-primary-portrait", "stop", "stop-screen-share",
			"storage", "store", "store-mall-directory", "straighten", "streetview", "strikethrough-s", "style",
			"subdirectory-arrow-left", "subdirectory-arrow-right", "subject", "subscriptions", "subtitles", "subway",
			"supervisor-account", "surround-sound", "swap-calls", "swap-horiz", "swap-vert", "swap-vertical-circle",
			"switch-camera", "switch-video", "sync", "sync-disabled", "sync-problem", "system-update",
			"system-update-alt", "tab", "tab-unselected", "tablet", "tablet-android", "tablet-mac", "tag-faces",
			"tap-and-play", "terrain", "text-fields", "text-format", "textsms", "texture", "theaters", "thumb-down",
			"thumb-up", "thumbs-up-down", "time-to-leave", "timelapse", "timeline", "timer", "timer-10", "timer-3",
			"timer-off", "title", "toc", "today", "toll", "tonality", "touch-app", "toys", "track-changes", "traffic",
			"train", "tram", "transfer-within-a-station", "transform", "translate", "trending-down", "trending-flat",
			"trending-up", "tune", "turned-in", "turned-in-not", "tv", "unarchive", "undo", "unfold-less",
			"unfold-more", "update", "usb", "verified-user", "vertical-align-bottom", "vertical-align-center",
			"vertical-align-top", "vibration", "video-call", "video-label", "video-library", "videocam", "videocam-off",
			"videogame-asset", "view-agenda", "view-array", "view-carousel", "view-column", "view-comfy",
			"view-compact", "view-day", "view-headline", "view-list", "view-module", "view-quilt", "view-stream",
			"view-week", "vignette", "visibility", "visibility-off", "voice-chat", "voicemail", "volume-down",
			"volume-mute", "volume-off", "volume-up", "vpn-key", "vpn-lock", "wallpaper", "warning", "watch",
			"watch-later", "wb-auto", "wb-cloudy", "wb-incandescent", "wb-iridescent", "wb-sunny", "wc", "web",
			"web-asset", "weekend", "whatshot", "widgets", "wifi", "wifi-lock", "wifi-tethering", "work", "wrap-text",
			"youtube-searched-for", "zoom-in", "zoom-out", "zoom-out-map" };
	private static String[] meses = { "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
			"DEZ" };

	public static void saveCsvFile(String path, List<String[]> content, String[] header) throws IOException {
		FileWriter out = new FileWriter(path);
		CSVPrinter printer = CSVFormat.DEFAULT.withHeader(header).print(out);
		for (Object[] record : content)
			printer.printRecord(record);

		printer.close();
		out.close();
	}

	public static void saveImage(String imageUrl, String path) throws IOException {
		URL url = new URL(imageUrl);
		String destName = path;

		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destName);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

	public static int getMesInteger(String mes) {
		for (int i = 0; i < meses.length; i++) {
			if (meses[i].equals(mes))
				return i + 1;
		}

		return -1;
	}

	public static boolean isNullEmpty(String texto) {
		return texto == null || texto.equals("");
	}

	public static String getFileContentType(String name) {
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			return fileNameMap.getContentTypeFor(name);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getMesExtenso(int mes) {
		return meses[mes - 1];
	}

	public static String getMesExtenso(String mesAno) {
		int mes = Integer.parseInt(mesAno.split("/")[0]);
		return meses[mes - 1] + "/" + mesAno.split("/")[1];
	}

	public static String getPrimeiroDia(String mesAno) {
		return "01/" + mesAno;
	}

	public static String getPrimeiroDiaProximoMes(String mesAno) {
		String mesStr = mesAno.split("/")[0];
		String anoStr = mesAno.split("/")[1];
		Integer mes = Integer.parseInt(mesStr);
		mes = mes + 1;
		if (mes < 10)
			mesStr = "0" + mes;

		return "01/" + mesStr + "/" + anoStr;
	}

	public static String getExtensaoArquivo(String name) {
		try {
			int lastIndex = name.lastIndexOf(".");
			return name.substring(lastIndex, name.length());
		} catch (Exception e) {
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListaValida(List lista) {
		return lista != null && lista.size() > 0;
	}

	public static java.sql.Date getStringParaData(String d1) throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		java.sql.Date data = null;
		try {
			data = new java.sql.Date(formatter.parse(d1).getTime());
		} catch (ParseException e) {
			throw new Exception(e.getMessage());
		}

		return data;
	}

	public static String formatarNumero(long numero, int posicoes) {
		String res = String.valueOf(numero);
		while (res.length() < posicoes)
			res = "0" + res;

		return res;
	}

	public static String formatarValor(double valor) {
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		return f.format(valor);
	}

	public static String getValorPrint(double valor) {
		NumberFormat moneyFormat = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
		moneyFormat.setMinimumFractionDigits(2);
		moneyFormat.setMaximumFractionDigits(2);
		return moneyFormat.format(valor);
	}

	public static double getValorFormatado(String valor) throws ParseException {
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		return f.parse(valor).doubleValue();
	}

	public static String gerarHash(String texto) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(texto.getBytes());
		byte[] hash = md.digest();

		StringBuilder s = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			int parteAlta = ((hash[i] >> 4) & 0xf) << 4;
			int parteBaixa = hash[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();

	}

	public static String gerarBase64(String texto) {
		return Base64.getEncoder().encodeToString(texto.getBytes());
	}

	public static String lerBase64(String texto) {
		return new String(Base64.getDecoder().decode(texto));
	}

	public static String pularLinha() {
		return System.getProperty("line.separator");
	}

	public static Date ParseStringDate(String texto) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date data = null;
		try {
			data = format.parse(texto);
		} catch (Exception e) {
		}
		return data;
	}

	public static Date ParseStringDate(String texto, String formato) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formato);
		Date data = null;
		try {
			data = format.parse(texto);
		} catch (Exception e) {
		}
		return data;
	}

	public static String getStringDeData(Date data) {
		if (data != null) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.format(data).toString();
		}
		return "";
	}

	public static String getStringDeData(Date data, String formato) {
		DateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(data).toString();
	}

	public static String formataDataPadrao(String data) {
		String[] auxiliar = data.split("-");

		data = auxiliar[2] + "/" + auxiliar[1] + "/" + auxiliar[0];

		return data;
	}

	public static String formataDataPadrao(java.sql.Date dateSql) {
		String data = dateSql.toString();

		String[] auxiliar = data.split("-");

		data = auxiliar[2] + "/" + auxiliar[1] + "/" + auxiliar[0];

		return data;
	}

	public static String getResumoTexto(String texto) {
		String resul = "";

		if (texto.length() >= 50) {
			resul = texto.substring(0, 49) + "...";
			return resul;
		} else {
			return texto;
		}

	}

	public static int tryParse(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String FormataTexto(String texto) {
		String resul = "";

		resul = texto.replace("\n", "<br/>");

		return resul;
	}

	public static void copiarArquivo(File fonte, File destino, boolean apagarFonte) throws IOException {
		verificarDiretorio(destino.getParentFile());

		InputStream in = new FileInputStream(fonte);
		OutputStream out = new FileOutputStream(destino);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();

		try {
			if (apagarFonte)
				fonte.delete();
		} catch (Exception e) {
		}
	}

	public static void copiarArquivo(String fonte, String destino) throws IOException {
		File fFonte = new File(fonte);
		File fDestino = new File(destino);

		verificarDiretorio(fDestino.getParentFile());

		InputStream in = new FileInputStream(fFonte);
		OutputStream out = new FileOutputStream(fDestino);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static void gravarArquivo(byte[] conteudo, String caminho) throws IOException {
		OutputStream out = new FileOutputStream(caminho);
		out.write(conteudo);
		out.close();
	}

	public static void verificarDiretorio(File dir) {
		if (!dir.isDirectory())
			dir.mkdirs();
	}

	public static void verificarDiretorio(String str) {
		File dir = new File(str);

		if (!dir.isDirectory())
			dir.mkdirs();
	}

	/**
	 * Remover acentuacao de Strings
	 */
	public static String removerAcentuacao(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");
		return texto;
	}

	public static String retirarCaracteresEspeciais(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^a-zZ-Z0-9]", "");
		return texto;
	}

	public static String normalizarNomeArquivo(String nome) {
		nome = removerAcentuacao(nome);
		nome = nome.replace(" ", "_");
		return nome;
	}

	public static String getContextFromUrl(String url) {
		String protocol = "https://";
		url = url.replace(protocol, "");
		int lastIndexOf = url.indexOf("/");
		try {
			return protocol + url.substring(0, lastIndexOf);
		} catch (Exception e) {
			return "";
		}
	}

}